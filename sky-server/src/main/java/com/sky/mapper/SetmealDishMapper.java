package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    List<Long> getByDishIds(List<Long> ids);

    void insertSetmealDishsBatch(List<SetmealDish> setmealDishes);

    List<SetmealDish> getByDishId(Long id);

    void insert(SetmealDish setmealDish);

    void update(SetmealDish setmealDish);

    void deleteBySetmealIds(List<Long> ids);
}
