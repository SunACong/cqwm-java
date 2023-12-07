package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api("管理端-订单管理")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单搜索：{}", ordersPageQueryDTO);

        PageResult pageResult = orderService.page(ordersPageQueryDTO);

        return Result.success(pageResult);
    }


    /**
     * 通过GET请求获取指定订单的详细信息
     *
     * @param id 订单ID
     * @return 返回订单详细信息的结果
     */
    @GetMapping("/details/{id}")
    @ApiOperation("查看订单详情")
    public Result<OrderVO> orderDetail(@PathVariable("id") Long id) {
        log.info("查看订单详情：{}", id);

        OrderVO orderVO = orderService.orderDetail(id);

        return Result.success(orderVO);
    }


    /**
     * 取消订单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result<String> cancel(@RequestBody OrdersCancelDTO orderCancelDTO) {
        log.info("取消订单：{}", orderCancelDTO);

        orderService.cancel(orderCancelDTO);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

    @GetMapping("/statistics")
    @ApiOperation("统计订单情况")
    public Result<OrderStatisticsVO> statistics() {
        log.info("统计订单情况");

        OrderStatisticsVO orderStatisticsVO = orderService.statistics();

        return Result.success(orderStatisticsVO);
    }

    /**
     * 确认接单
     *
     * @param ordersConfirmDTO 订单确认DTO
     * @return 操作结果
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result<String> confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单：{}", ordersConfirmDTO);

        orderService.confirm(ordersConfirmDTO);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result<String> delivery(@PathVariable("id") Long id) {
        log.info("派送订单：{}", id);

        orderService.delivery(id);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result<String> rejection(@RequestBody OrdersRejectionDTO orderRejectionDTO) {
        log.info("拒单：{}", orderRejectionDTO);

        orderService.rejection(orderRejectionDTO);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }


    /**
     * 完成订单
     *
     * @param id 订单ID
     * @return 返回操作结果，成功时为MessageConstant.OPERATE_SUCCESS
     */
    @PutMapping("complete/{id}")
    @ApiOperation("完成订单")
    public Result<String> complete(@PathVariable("id") Long id) {
        log.info("完成订单：{}", id);

        orderService.complete(id);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

}
