package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;


    /**
     * 定时任务，用于处理超时未支付的订单。
     * 使用 cron 表达式进行定时，每小时的第0分钟执行。
     *
     * @Scheduled(cron = "0 * * * * ? ")
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void processTimeoutOrder() {
        log.info("订单超时处理");

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, time);
        for (Orders orders : ordersList) {
            orders.setStatus(Orders.CANCELLED);
            orders.setCancelReason("订单超时未支付");
            orders.setCancelTime(LocalDateTime.now());
            orderMapper.update(orders);
        }
    }



    /**
     * 定时任务，已支付，递送状态未改变订单
     * 定时任务，每天凌晨1点执行
     */
    @Scheduled(cron = "0 0 1 ? * ? ")
    public void processUnpaidOrder() {
        log.info("已支付，递送状态未改变订单");

        LocalDateTime time = LocalDateTime.now().plusHours(-1);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, time);
        for (Orders orders : ordersList) {
            orders.setStatus(Orders.COMPLETED);
            orders.setCancelReason("订单超时未配送订单");
            orders.setCancelTime(LocalDateTime.now());
            orderMapper.update(orders);
        }
    }

}
