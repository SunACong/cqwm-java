package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出登录")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 保存员工信息
     * @param employeeDTO 前端DTO对象
     * @return 是否成功
     */
    @PostMapping
    @ApiOperation(value = "新增员工")
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("员工信息：{}", employeeDTO);

        employeeService.sava(employeeDTO);

        return Result.success("操作成功");
    }

    /**
     * 分页查询
     * @param employeePageQueryDTO 分页信息
     * @return 分页数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页信息：{}", employeePageQueryDTO);

        PageResult pageResult = employeeService.page(employeePageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 员工的启用或禁用
     * @param status 员工状态
     * @param id 员工id
     * @return 操作结果
     */

    @PostMapping("/status/{status}")
    @ApiOperation(value = "员工启用/停用")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        log.info("员工启用/停用：{},{}", status, id);

        employeeService.update(status, id);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

    /**
     * 根据id查询员工
     * @param id 员工id
     * @return 员工信息
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "员工详情")
    public Result<Employee> getById(@PathVariable Integer id) {

        log.info("员工id：{}", id);

        Employee employee = employeeService.getById(id);

        return Result.success(employee);
    }


    /**
     * 更新员工信息
     *
     * @param employeeDTO 员工信息DTO
     * @return 返回操作结果，成功时包含"操作成功"的字符串
     */
    @PutMapping
    @ApiOperation(value = "更新员工信息")
    public Result<String> update(@RequestBody EmployeeDTO employeeDTO) {

        log.info("员工信息：{}", employeeDTO);

        employeeService.update(employeeDTO);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

}
