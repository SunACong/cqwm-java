package com.sky.controller.user;


import com.sky.constant.MessageConstant;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
@Api(tags = "小程序-订单接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 处理用户下单请求，并返回订单提交结果。
     *
     * @param ordersSubmitDTO 下单请求的数据传输对象
     * @return 返回订单提交结果
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> list(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单信息：{}", ordersSubmitDTO);

        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);

        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }


    /**
     * 获取历史订单信息
     *
     * @param page 页码
     * @param pageSize 每页数量
     * @param status 订单状态
     * @return 返回查询结果，包括分页信息和订单列表
     */
    @GetMapping("/historyOrders")
    @ApiOperation("查询历史订单")
    public Result<PageResult> historyOrders(Integer page, Integer pageSize, Integer status) {
        log.info("查询历史订单：{}页{}条{}状态", page, pageSize, status);

        PageResult pageResult = orderService.historyOrders(page, pageSize, status);

        return Result.success(pageResult);
    }

    @GetMapping("/reminder/{id}")
    @ApiOperation("催单")
    public Result<String> reminder(@PathVariable("id") Long id) {
        log.info("催单：{}", id);
        orderService.reminder(id);
        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }
}

