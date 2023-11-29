package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 保存员工信息
     * @param employeeDTO
     * @return
     */
    void sava(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     * @param employeePageQueryDTO 分页信息
     * @return 分页数据
     */
    PageResult page(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用或禁用
     * @param status 状态
     * @param id 主键
     */
    void update(Integer status, Long id);

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    Employee getById(Integer id);

    /**
     * 更新员工信息
     *
     * @param employeeDTO 包含员工信息的对象
     */
    void update(EmployeeDTO employeeDTO);
}
