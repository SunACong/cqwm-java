package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.anatation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishMapper {

    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    Dish getById(Long id);

    void deleteById(Long id);

    void deleteByIds(List<Long> ids);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    List<Dish> list(Long categoryId);
}
