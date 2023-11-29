package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(value = "套餐管理")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;



    /**
     * 新增套餐
     *
     * @param setmealDTO 套餐信息
     * @return 操作结果
     */
    @PostMapping
    @ApiOperation(value = "新增套餐")
    public Result<String> insert(@RequestBody SetmealDTO setmealDTO) {
        log.info("insert setmealDTO = {} ", setmealDTO);
        setmealService.insert(setmealDTO);
        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }


    /**
     * 通过 GET 请求分页查询套餐信息。
     *
     * @param setmealPageQueryDTO 分页查询套餐信息的参数类。
     * @return 返回查询结果的包装类 Result<PageResult>，其中 PageResult 为分页查询结果类。
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询套餐")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("setmealPageQueryDTO 分页查询 = {}", setmealPageQueryDTO);
        PageResult pageResults =  setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResults);
    }


    /**
     * 根据id查询套餐
     *
     * @param id 套餐id
     * @return 返回查询到的套餐详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("查询套餐id = {}", id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }


    /**
     * 修改套餐
     *
     * @param setmealDTO 套餐信息
     * @return 返回操作结果，包含成功消息
     */
    @PutMapping
    @ApiOperation("修改套餐")
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {

        log.info("修改套餐 = {}", setmealDTO);

        setmealService.update(setmealDTO);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }



    /**
     * 批量删除套餐
     *
     * @param ids 套餐ID列表
     * @return 返回操作结果，成功时为"{}"
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public Result<String> delete(@RequestParam List<Long> ids) {

        log.info("批量删除套餐 = {}", ids);

        setmealService.delete(ids);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

    /**
     * 根据状态对套餐进行起售/停售操作
     *
     * @param status 套餐状态：0-停售，1-起售
     * @param id 套餐id
     * @return 操作结果，成功返回
     */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售/停售")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {

        log.info("套餐id = {}", id);

        setmealService.startOrStop(status, id);

        return Result.success(MessageConstant.OPERATE_SUCCESS);

    }

}
