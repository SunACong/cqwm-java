package com.sky.mapper;

import com.sky.anatation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    @AutoFill(OperationType.INSERT)
    void insertBatch(List<DishFlavor> dishFlavors);

    void deleteByDishId(Long dishId);

    void deleteByDishIds(List<Long> ids);

    List<DishFlavor> getByDishId(Long id);

    @AutoFill(OperationType.INSERT)
    void insert(DishFlavor flavor);

    @AutoFill(OperationType.UPDATE)
    void update(DishFlavor flavor);
}
