package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;

import java.util.List;

public interface DishService {

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO 菜品查询参数
     * @return 分页查询结果
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 新增菜品
     *
     * @param dishDTO 菜品信息
     */
    void insert(DishDTO dishDTO);

    /**
     * 批量删除菜品
     *
     * @param ids 菜品ID数组
     */
    void delete(List<Long> ids);

    /**
     * ID查询菜品
     * @param id 菜品ID
     * @return 查询菜品信息
     */
    DishDTO getById(Long id);


    /**
     * 更新菜品信息
     *
     * @param dishDTO 包含更新信息的菜品DTO
     */
    void update(DishDTO dishDTO);

    /**
     * 启动或停止某个服务
     *
     * @param status 服务状态，0表示停止，1表示启动
     * @param id 服务ID
     */
    void startOrStop(Integer status, Long id);

    /**
     * 查询菜品列表
     *
     * @param categoryId 菜品分类ID
     * @return 返回包含所查询菜品列表的结果
     */
    List<Dish> list(Long categoryId);
}
