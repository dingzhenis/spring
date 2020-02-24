package springbootdemo.cache.mapper;

import springbootdemo.cache.bean.Employee;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EmployeeMapper {
    @Select("Select * from EmployeeService where id = #{id}")
    public Employee getEmpById(Integer id);
    @Insert("insert into employee (lastName,email,gender,d_Id) values(#{lastName},#{email},#{gender},#{dId})")
    public void insertEmp(Employee employee);

    @Update("update EmployeeService set lastName=#{lastName},email=#{email},gender=#{gender},d_id=#{dId} where id=#{id}")
    public void updateEmp(Employee employee);

    @Delete("delete from EmployeeService where id=#{id}")
    public void deleteEmp(Integer id);

    @Select("select * from EmployeeService where lastName=#{lastName}")
    public Employee getEmpbyLastName(String lastName);
}
