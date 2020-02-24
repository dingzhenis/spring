package springbootdemo.cache.service;

import springbootdemo.cache.bean.Department;
import springbootdemo.cache.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

@Service
public class DeptService {
    @Autowired
    DepartmentMapper departmentMapper;
    @Qualifier("redisCacheManager") //标记缓存管理器的id
    @Autowired
    RedisCacheManager redisCacheManager;
    public Department getDeptId(Integer id) {
        System.out.println("check successful"+id);
        Department department = departmentMapper.getDeptById(id);

        //获取缓存
        Cache dept = redisCacheManager.getCache("dept");
        //操作缓存
        dept.put("dept:1",department);
        return department;
    }
}
