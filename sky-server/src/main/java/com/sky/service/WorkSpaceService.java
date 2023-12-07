package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkSpaceService {
    BusinessDataVO businessData(LocalDateTime beginTime, LocalDateTime endTime);

    SetmealOverViewVO overviewSetmeals(LocalDateTime beginTime, LocalDateTime endTime);

    DishOverViewVO overviewDishes(LocalDateTime beginTime, LocalDateTime endTime);

    OrderOverViewVO overviewOrders();
}
