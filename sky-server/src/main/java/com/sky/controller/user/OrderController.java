package com.sky.controller.user;


import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}

