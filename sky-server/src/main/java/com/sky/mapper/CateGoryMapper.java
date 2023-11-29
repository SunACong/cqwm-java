package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.anatation.AutoFill;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CateGoryMapper {
    @AutoFill(OperationType.INSERT)
    void insert(Category category);

    Page<Category> list(Category category);

    @AutoFill(OperationType.UPDATE)
    void startOrStop(Category category);

    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    void delete(Long id);
}
