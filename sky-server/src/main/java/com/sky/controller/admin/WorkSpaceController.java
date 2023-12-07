package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/admin/workspace")
@Api("工作台")
@Slf4j
public class WorkSpaceController {

    @Autowired
    private WorkSpaceService workSpaceService;


    /**
     * 查询进入运营数据
     *
     * @return 返回操作结果，封装了运营数据
     */
    @GetMapping("/businessData")
    @ApiOperation("查询进入运营数据")
    public Result<BusinessDataVO> businessData() {
        log.info("查询今日运营数据");
        LocalDateTime beginTime = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.MAX);

        BusinessDataVO businessData = workSpaceService.businessData(beginTime, endTime);


        return Result.success(businessData);
    }


    /**
     * 查询套餐总览
     *
     * @return 返回套餐总览结果
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation("套餐总览")
    public Result<SetmealOverViewVO> overviewSetmeals() {
        log.info("查询套餐总览");
        LocalDateTime beginTime = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.MAX);

        SetmealOverViewVO setmealOverViewVO =  workSpaceService.overviewSetmeals(beginTime, endTime);


        return Result.success(setmealOverViewVO);
    }


    /**
     * 菜品总览
     *
     * @return 返回菜品总览结果
     */
    @GetMapping("/overviewDishes")
    @ApiOperation("菜品总览")
    public Result<DishOverViewVO> overviewDishes() {
        log.info("查询菜品总览");
        LocalDateTime beginTime = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.MAX);

        DishOverViewVO dishOverViewVO =  workSpaceService.overviewDishes(beginTime, endTime);


        return Result.success(dishOverViewVO);
    }

    @GetMapping("/overviewOrders")
    @ApiOperation("订单总览")
    public Result<OrderOverViewVO> overviewOrders() {

        log.info("查询订单总览");

        OrderOverViewVO orderOverViewVO =  workSpaceService.overviewOrders();

        return Result.success(orderOverViewVO);
    }
}
