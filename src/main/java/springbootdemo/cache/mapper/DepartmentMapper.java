package springbootdemo.cache.mapper;

import springbootdemo.cache.bean.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DepartmentMapper {
    @Select("Select * from department where id =#{id}")
    Department getDeptById(Integer id);
}
