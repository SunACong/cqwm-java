package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {


    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private WebSocketServer webSocketServer;



    /**
     * 提交订单
     *
     * @param ordersSubmitDTO 订单提交参数
     * @return 返回订单提交结果
     * @throws RuntimeException 如果地址为空或购物车为空，则抛出异常
     */
    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        // 校验地址是否为空
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        AddressBook addressBook = addressBookMapper.getById(addressBookId);
        if (addressBook == null) {
            throw new RuntimeException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        // 校验购物车是否为空
        Long currentId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                                                .userId(currentId)
                                                .build();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new RuntimeException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 插入order表
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setUserId(currentId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());

        orderMapper.insert(orders);

        // 插入order_detail表
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        // 清空购物车
        shoppingCartMapper.clean(currentId);

        // 返回VO
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderTime(orders.getOrderTime())
                .orderAmount(orders.getAmount())
                .build();

        return orderSubmitVO;
    }


    /**
     * 订单支付
     *
     * @param ordersPaymentDTO 订单支付DTO
     * @return 订单支付VO
     * @throws Exception 异常信息
     */
    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        ////调用微信支付接口，生成预支付交易单
        //JSONObject jsonObject = weChatPayUtil.pay(
        //        ordersPaymentDTO.getOrderNumber(), //商户订单号
        //        new BigDecimal(0.01), //支付金额，单位 元
        //        "苍穹外卖订单", //商品描述
        //        user.getOpenid() //微信用户的openid
        //);
        JSONObject jsonObject = new JSONObject();

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        paySuccess(ordersPaymentDTO.getOrderNumber());

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);

        Map map = new HashMap();
        map.put("type", 1);
        map.put("orderId", ordersDB.getId());
        map.put("content", "订单号:" + outTradeNo);
        String jsonString = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(jsonString);
    }


    /**
     * 查询订单历史记录
     *
     * @param page 页码
     * @param pageSize 每页数量
     * @param status 订单状态
     * @return 包含订单历史记录的分页结果
     */
    @Override
    public PageResult historyOrders(Integer page, Integer pageSize, Integer status) {

        PageHelper.startPage(page, pageSize);
        OrdersPageQueryDTO ordersPageQueryDTO = OrdersPageQueryDTO.builder()
                .status(status)
                .userId(BaseContext.getCurrentId())
                .build();

        Page<Orders> ordersPage = orderMapper.page(ordersPageQueryDTO);

        List<OrderVO> ordersVOList = new ArrayList<>();
        if (ordersPage.getResult() != null && ordersPage.size() > 0) {
            for (Orders orders : ordersPage.getResult()) {
                Long orderId = orders.getId();
                List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderId);
                OrderVO ordersVO = new OrderVO();
                BeanUtils.copyProperties(orders, ordersVO);
                ordersVO.setOrderDetailList(orderDetailList);
                ordersVOList.add(ordersVO);
            }
        }

        return new PageResult(ordersPage.getTotal(), ordersVOList);

    }

    /**
     * 发送订单提醒
     *
     * @param id 订单ID
     * @throws OrderBusinessException 订单业务异常
     */
    @Override
    public void reminder(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Map map = new HashMap();
        map.put("type", 2);
        map.put("orderId", id);
        map.put("content", "订单号:" + orders.getNumber());
        String jsonString = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(jsonString);
    }

    @Override
    public void cancel(OrdersCancelDTO orderCancelDTO) {
        Orders orders = Orders.builder()
                .id(orderCancelDTO.getId())
                .status(Orders.CANCELLED)
                .cancelTime(LocalDateTime.now())
                .cancelReason(orderCancelDTO.getCancelReason())
                .build();
        orderMapper.update(orders);
    }

    /**
     * 根据订单ID获取订单详情
     *
     * @param id 订单ID
     * @return 返回订单详情Result<OrderVO>对象
     */
    @Override
    public OrderVO orderDetail(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        } else {
            List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
            AddressBook addressBook = addressBookMapper.getById(orders.getAddressBookId());
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            orderVO.setAddress(addressBook.getProvinceName() + " " + addressBook.getCityName() + " " + addressBook.getDistrictName() + " " + addressBook.getDetail());
            orderVO.setOrderDetailList(orderDetailList);
            return orderVO;
        }
    }

    /**
     * 根据订单id，进行再来一单操作
     *
     * @param id 订单id
     * @return 操作结果，成功返回MessageConstant.OPERATE_SUCCESS
     */
    @Override
    public void repetition(Long id) {

    }


    @Override
    public PageResult page(OrdersPageQueryDTO ordersPageQueryDTO) {


        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> page = orderMapper.page(ordersPageQueryDTO);

        List<OrderVO> orderVOList = new ArrayList<>();

        page.getResult().forEach(orders -> {
            String orderDishesStr = getOrderDishesStr(orders);
            String orderAddressStr = getOrderAddressStr(orders);
            OrderVO ordersVO = new OrderVO();
            BeanUtils.copyProperties(orders, ordersVO);
            ordersVO.setOrderDishes(orderDishesStr);
            ordersVO.setAddress(orderAddressStr);
            orderVOList.add(ordersVO);
        });

        return new PageResult(page.getTotal(), orderVOList);
    }


    /**
     * 根据订单id获取菜品信息字符串
     *
     * @param orders
     * @return
     */
    private String getOrderDishesStr(Orders orders) {
        // 查询订单菜品详情信息（订单中的菜品和数量）
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将每一条订单菜品信息拼接为字符串（格式：宫保鸡丁*3；）
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getNumber() + ";";
            return orderDish;
        }).collect(Collectors.toList());

        // 将该订单对应的所有菜品信息拼接在一起
        return String.join("", orderDishList);
    }

    /**
     * 根据订单获取地址
     *
     * @param orders
     * @return
     */
    private String getOrderAddressStr(Orders orders) {
        AddressBook addressBook = addressBookMapper.getById(orders.getAddressBookId());

        return addressBook.getProvinceName() + " " + addressBook.getCityName() + " " + addressBook.getDistrictName() + " " + addressBook.getDetail();

    }

    /**
     * 统计订单数量
     *
     * @return 返回订单统计信息
     */
    @Override
    public OrderStatisticsVO statistics() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();

        Map map = new HashMap();

        map.put("status", Orders.DELIVERY_IN_PROGRESS);

        // 派送中
        Integer deliveryInProgress = orderMapper.countOrder(map);

        map.put("status", Orders.CONFIRMED);

        // 待派送
        Integer confirmed = orderMapper.countOrder(map);

        map.put("status", Orders.TO_BE_CONFIRMED);

        // 待接单
        Integer toBeConfirmed = orderMapper.countOrder(map);

        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);

        return orderStatisticsVO;
    }

    /**
     * 确认订单
     *
     * @param ordersConfirmDTO 订单确认数据传输对象
     */
    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {

        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();

        orderMapper.update(orders);
    }

    /**
     * 执行交付订单。
     *
     * @param id 订单ID
     */
    @Override
    public void delivery(Long id) {
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.DELIVERY_IN_PROGRESS)
                .build();

        orderMapper.update(orders);
    }

    /**
     * 拒绝订单
     *
     * @param orderRejectionDTO 包含拒绝订单信息的DTO对象
     */
    @Override
    public void rejection(OrdersRejectionDTO orderRejectionDTO) {
        Orders orders = Orders.builder()
                .id(orderRejectionDTO.getId())
                .status(Orders.CANCELLED)
                .rejectionReason(orderRejectionDTO.getRejectionReason())
                .build();

        orderMapper.update(orders);

    }

    /**
     * 完成指定订单
     *
     * @param id 订单ID
     */
    @Override
    public void complete(Long id) {
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.COMPLETED)
                .build();

        orderMapper.update(orders);
    }
}
