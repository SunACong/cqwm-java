package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {


    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO 菜品查询参数
     * @return 分页查询结果
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增菜品
     *
     * @param dishDTO 菜品信息
     */
    @Override
    @Transactional
    public void insert(DishDTO dishDTO) {
        Dish dish = new Dish();

        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.insert(dish);
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 批量删除菜品
     *
     * @param ids 菜品ID数组
     */
    @Override
    @Transactional
    public void delete(List<Long> ids) {
        // 判断菜品是否能够删除（是否起售）
        for (Long id: ids) {
            Dish dish = dishMapper.getById(id);
            if (StatusConstant.ENABLE.equals(dish.getStatus())) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 判断菜品是否能够删除（是否被套餐关联）
        List<Long> setmealIds = setmealDishMapper.getByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 删除菜品数据 & 删除菜品口味数据
        //for (Long id : ids) {
        //    dishMapper.deleteById(id);
        //    dishFlavorMapper.deleteByDishId(id);
        //}
        dishMapper.deleteByIds(ids);

        dishFlavorMapper.deleteByDishIds(ids);

    }

    /**
     * 根据ID查询菜品
     * @param id 菜品的ID
     * @return 菜品信息的Result对象
     */
    @Override
    public DishDTO getById(Long id) {
        DishDTO dishDTO = new DishDTO();

        // 获取菜品信息
        Dish dish = dishMapper.getById(id);

        // 根据dishId获取菜品口味信息
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);

        BeanUtils.copyProperties(dish, dishDTO);
        dishDTO.setFlavors(flavors);

        return dishDTO;
    }

    /**
     * 更新菜品信息及口味
     *
     * @param dishDTO 包含要更新的菜品信息的 DTO 对象
     */
    @Override
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.update(dish);

        for (DishFlavor flavor : dishDTO.getFlavors()) {
            if (flavor.getId() == null) {
                flavor.setDishId(dish.getId());
                dishFlavorMapper.insert(flavor);
            } else {
                dishFlavorMapper.update(flavor);
            }
        }
    }

    /**
     * 启动或停止菜品
     *
     * @param status 菜品状态
     * @param id 菜品ID
     * @return void
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();

        dishMapper.update(dish);
    }

    /**
     * 查询菜品列表
     *
     * @param categoryId 菜品分类ID
     * @return 返回包含所查询菜品列表的结果
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        return dishMapper.list(dish);
    }


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

}
