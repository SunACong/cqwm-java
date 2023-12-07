package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);


    /**
     * 历史订单列表
     * @param page
     * @param pageSize
     * @param status
     * @return 订单列表
     */
    PageResult historyOrders(Integer page, Integer pageSize, Integer status);

    void reminder(Long id);

    void cancel(OrdersCancelDTO orderCancelDTO);

    OrderVO orderDetail(Long id);

    void repetition(Long id);

    PageResult page(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statistics();

    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    void delivery(Long id);

    void rejection(OrdersRejectionDTO orderRejectionDTO);

    void complete(Long id);
}
