package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper  dishMapper;


    @Override
    public BusinessDataVO businessData(LocalDateTime beginTime, LocalDateTime endTime) {

        HashMap map = new HashMap();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);

        // 总订单数
        Integer totalOrders = orderMapper.countOrder(map);

        map.put("status", Orders.COMPLETED);

        // 营业额
        Double turnover = orderMapper.turnoverStatisticsByMap(map);
        turnover = turnover == null ? 0.0 : turnover;

        // 有效订单数
        Integer validOrderCount = orderMapper.countOrder(map);

        // 新增用户数
        Integer newUsers = userMapper.userStatisticsByMap(map);
        newUsers = newUsers == null ? 0 : newUsers;

        Double unitPrice = 0.0;
        Double orderCompletionRate = 0.0;

        if (totalOrders != 0 && validOrderCount != 0) {
            // 订单完成率
            orderCompletionRate = (double) validOrderCount / totalOrders;
            // 平均客单价
            unitPrice = turnover / validOrderCount;
        }




        return BusinessDataVO.builder()
               .newUsers(newUsers)
               .validOrderCount(validOrderCount)
               .orderCompletionRate(orderCompletionRate)
               .unitPrice(unitPrice)
               .turnover(turnover)
               .build();
    }


    /**
     * 获取给定时间范围内的套餐售卖情况
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return SetmealOverViewVO 套餐售卖情况对象
     */
    @Override
    public SetmealOverViewVO overviewSetmeals(LocalDateTime beginTime, LocalDateTime endTime) {

        Map map = new HashMap();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);

        map.put("status", StatusConstant.ENABLE);

        // 起售套餐
        Integer sold = setmealMapper.overviewSetmeals(map);

        map.put("status", StatusConstant.DISABLE);

        // 停售套餐
        Integer discontinued = setmealMapper.overviewSetmeals(map);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 获取菜品售卖情况统计信息
     *
     * @param beginTime 查询起始时间
     * @param endTime   查询结束时间
     * @return DishOverViewVO 菜品售卖情况统计信息
     */
    @Override
    public DishOverViewVO overviewDishes(LocalDateTime beginTime, LocalDateTime endTime) {

        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);

        // 起售套餐
        Integer sold = dishMapper.overviewDishes(map);

        map.put("status", StatusConstant.DISABLE);

        // 停售套餐
        Integer discontinued = dishMapper.overviewDishes(map);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 获取订单概述信息
     *
     * @return OrderOverViewVO 订单概述信息
     */
    @Override
    public OrderOverViewVO overviewOrders() {
        Map map = new HashMap();

        // 所有订单
        Integer allOrders = orderMapper.countOrder(map);

        map.put("status", Orders.TO_BE_CONFIRMED);

        //待接单数量
        Integer waitingOrders = orderMapper.countOrder(map);

        map.put("status", Orders.CONFIRMED);

        //待派送数量
        Integer deliveredOrders = orderMapper.countOrder(map);

        map.put("status", Orders.COMPLETED);

        //已完成
        Integer completedOrders = orderMapper.countOrder(map);

        map.put("status", Orders.CANCELLED);

        //已取消
        Integer cancelledOrders = orderMapper.countOrder(map);


        return OrderOverViewVO.builder()
                                .allOrders(allOrders)
                                .waitingOrders(waitingOrders)
                                .deliveredOrders(deliveredOrders)
                                .completedOrders(completedOrders)
                                .cancelledOrders(cancelledOrders)
                                .build();
    }
}
