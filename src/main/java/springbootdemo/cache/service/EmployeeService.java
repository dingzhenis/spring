package springbootdemo.cache.service;

import springbootdemo.cache.bean.Employee;
import springbootdemo.cache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

@CacheConfig(cacheNames = "emp",cacheManager = "redisCacheManager")//抽取缓存的公共属性
@Service
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    /**
     *  将方法的运行结果存储到缓存，以后再要相同的数据，就可以直接从缓存中提取，不用调用方法了
     *
     *  CacheManager管理多个Cache组件，对缓存的真正的Crud操作在Cache组件中，每一个缓存组件都有一个自己的名字
     *
     *  属性解释：
     *  cacheName/value : 指定缓存组件的名字;讲方法的返回结果放在哪个缓存中，是数组的方式，可以指定多个缓存
     *
     *  key:缓存数据使用的key，可以用它来指定，默认是使用方法参数的值   比如我们传入一个Integer id，那么这个id就是返回值
     *  将id通过KV表达式保存起来
     *  #id表示参数的值，等同于#a0 #p0 #root.args[0]
     *
     *  以此为例@Cacheable(value="emp",key="#id")意思就是缓存组件名字是emp，缓存数据的key是id的值，#代表取出值
     *  编写spel表达式的时候要知道 #id等同于#a0 #p0 #root.args[0]
     *  keyGenerator：key的生成器，自己制定key的生成id
     *  key/keyGenerator二选一
     *  【此功能以cache.MycacheConfig为例】
     *  编写自定义的keyGenerator，让Cacheable()内的keyGenerator可以取代key(二选一)
     *
     *
     *  cacheManager：指定缓存管理器或者使用cacheResolver指定获取解析器
     *  condition：指定符合条件的情况下才进行缓存
     *  condition = "#{id>0}"
     *  unless：否定缓存，当unless指定的条件为true的时候，方法的返回值就不会被缓存，可以获取到结果进行判断
     *  unless="#result==null"
     *  sync:是否使用异步模式
     *
     *
     *  原理：
     *  1.自动配置类CacheAutoConfiguration
     *  2.配置缓存类
     *
     *
     *  运行流程
     *  @Cacheable：
     *  1.方法运行之前先去检查Cache(缓存组件)，按照cacheNames指定的名字获取;
     *  (CacheManager先获取对应的缓存)，第一次获取缓存如果没有Cache组件会自动创建
     *  2.去Cache中查找缓存的内容，使用一个Key，默认就是方法的参数；
     *  key是按照某种策略生成的，默认就是使用KeyGenerator生成的
     *      SimpleGenerator生成key的策略：
     *          (1)如果没有参数：key=new Simple()
     *          (2)如果有一个参数：key=参数的值
     *          (3)如果有一个参数：key=new SimpleKey(params);
     *  3.没有查到缓存就调用目标方法
     *  4.将目标的方法返回的结果，放进缓存中
     *
     * @Cacheable标注的方法执行之前先检查缓存中是否存在，默认按照参数的值生成key去查询缓存，如果没有就将结果放入缓存。
     *
     *
     * 核心：
     * 1.使用CacheManager【ConcurrentMapCacheManager】按照名字得到Cache【ConcurrentMapCurrent】组件
     * 2.key使用keyGenerator生成的，默认是SimpleKeyGenerator
     * @param id
     * @return
     */

    @Cacheable(cacheNames = {"emp"},keyGenerator = "myKeyGeneratorConfig")
    public Employee getEmp(Integer id) {
        System.out.println("Number："+id+"is selecting");
        Employee employee = employeeMapper.getEmpById(id);
        return employee;
    }

    /**
     * @CachePut:调用方法+更新缓存数据 修改数据库某个数据并且更新缓存
     *
     * 调用时机
     * 1.先调用目标方法
     * 2.将目标方法的结果缓存起来
     *
     * 测试步骤
     * 1.查询1001号员工，结果会放到缓存中
     * @存储的方法类似于KV存储方法，但是key是内部自然生成且自增的
     * 2.以后查询还是之前的结果
     * 3.即使缓存保留后，最数据库进行操作，也不影响缓存，缓存会自动进行更新操作
     * @更新之后传入的实际上是一个employee的对象，value是更新的内容，而不是直接对应着，之前自动生成的key去更新
     * @所以我们就要自动指定出来key，去做到真正的自动更新
     *
     * 例：key = "#employee.id":使用传入的参数的id
     *     key = "#result.id":使用返回的参数id
     *     但是@cacheable的key不能用result，因为@cacheable是在查找前搜索id，而@CachePut可以用，因为是缓存完了更新完了去搜索id
     */

    @CachePut(value = "emp",key = "#result.id")
    public Employee updateEmp(Employee employee) {
        employeeMapper.updateEmp(employee);
        return employee;
    }

    /**
     * @CacheEvict : 缓存清除
     * key:指定要清除的数据
     * allEntries :选择是否删除所有缓存  true就是删除所有的了
     * beforeInvocation = false ： 缓存的清除是否在方法之前执行
     * 默认代表是在方法执行之后执行，但是如果是true就是在执行方法之前清除缓存，不论方法是否有异常
     */
    @CacheEvict(value = "emp")
    public void deleteEmp(Integer id){
        employeeMapper.deleteEmp(id);
    }


    @Caching(
            cacheable = {
                    @Cacheable(value = "emp",key = "#lastName")
            },
            put = {
                    @CachePut(value = "emp",key = "#result.id"),
                    @CachePut(value = "emp",key = "result.email")
            }
    )
    public Employee getEmpBylastName(String lastName) {
        return employeeMapper.getEmpbyLastName(lastName);
    }
}
