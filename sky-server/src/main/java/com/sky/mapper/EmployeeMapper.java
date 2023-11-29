package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.anatation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    /**
     * 新增员工
     * @param employee
     * @return
     */
    @Insert("insert into employee(username, password, name, phone, sex, id_number, create_user, create_time, update_user, update_time, status) " +
            "values" +
            "(#{username},#{password},#{name},#{phone},#{sex},#{idNumber},#{createUser},#{createTime},#{updateUser},#{updateTime},#{status})")
    @AutoFill(OperationType.INSERT)
    void insert(Employee employee);

    /**
     * 分页查询员工信息
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用/停用员工
     * @param employee
     */
    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 根据ID获取员工对象
     *
     * @param id 员工ID
     * @return 对应的员工对象
     */
    Employee getById(Integer id);
}
