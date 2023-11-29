package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CateGoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CateGoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CateGoryServiceImpl implements CateGoryService {

    @Autowired
    private CateGoryMapper cateGoryMapper;


    /**
     * 保存分类信息
     *
     * @param categoryDTO 分类信息DTO
     */
    @Override
    public void insert(CategoryDTO categoryDTO) {

        Category category = new Category();

        BeanUtils.copyProperties(categoryDTO, category);

        // 默认禁用分类或者菜品
        category.setStatus(StatusConstant.DISABLE);
        // 创建 修改时间
        //category.setCreateTime(LocalDateTime.now());
        //category.setUpdateTime(LocalDateTime.now());
        // 设置创建人和修改人
        //category.setCreateUser(BaseContext.getCurrentId());
        //category.setUpdateUser(BaseContext.getCurrentId());

        cateGoryMapper.insert(category);
    }

    /**
     * 分页查询类别信息
     *
     * @param categoryPageQueryDTO 分页查询类别信息的参数
     * @return 返回分页查询结果
     */
    @Override
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {

        Category cateGory = new Category();

        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        BeanUtils.copyProperties(categoryPageQueryDTO, cateGory);

        Page<Category> page = cateGoryMapper.list(cateGory);

        return new PageResult(page.getTotal(), page.getResult());
    }



    /**
     * 开始或停止分类
     *
     * @param status 状态：0-停止，1-开始
     * @param id 分类ID
     * @return void
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                                    .id(id)
                                    .status(status)
                                    .build();
        cateGoryMapper.startOrStop(category);

    }

    /**
     * 返回指定类型的分类列表
     *
     * @param type 分类类型
     * @return 分类列表
     */
    @Override
    public List<Category> list(Integer type) {
        Category category = Category.builder()
                .type(type)
                .build();

        return cateGoryMapper.list(category);
    }

    /**
     * 更新分类信息
     *
     * @param categoryDTO 分类信息DTO
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();

        BeanUtils.copyProperties(categoryDTO, category);

        cateGoryMapper.update(category);
    }

    /**
     * 根据ID删除分类
     *
     * @param id 分类ID
     */
    @Override
    public void delete(Long id) {
        cateGoryMapper.delete(id);
    }
}
