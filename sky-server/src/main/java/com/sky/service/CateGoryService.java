package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CateGoryService {
    /**
     * 保存类别信息
     *
     * @param categoryDTO 类别信息DTO
     */
    void insert(CategoryDTO categoryDTO);

    /**
     * 根据分类页面查询DTO分页获取商品列表
     *
     * @param categoryPageQueryDTO 分类页面查询DTO
     * @return 分页结果集
     */
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);


    /**
     * 启动或停止。
     *
     * @param status 状态
     * @param id ID
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据类型获取分类列表
     *
     * @param type 分类类型
     * @return 分类列表
     */
    List<Category> list(Integer type);

    /**
     * 更新分类信息
     *
     * @param categoryDTO 分类数据传输对象
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 根据ID删除记录。
     *
     * @param id 要删除的记录的ID。
     */
    void delete(Long id);
}
