package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;


    /**
     * 新增套餐
     *
     * @param setmealDTO 套餐信息
     */
    @Override
    @Transactional
    public void insert(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();

        BeanUtils.copyProperties(setmealDTO, setmeal);

        setmealMapper.insert(setmeal);

        Long setmealId = setmeal.getId();

        if (setmealDTO.getSetmealDishes()!= null && setmealDTO.getSetmealDishes().size() > 0) {
            setmealDTO.getSetmealDishes().forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
            setmealDishMapper.insertSetmealDishsBatch(setmealDTO.getSetmealDishes());
        }
    }


    /**
     * 通过 GET 请求分页查询套餐信息。
     *
     * @param setmealPageQueryDTO 分页查询套餐信息的参数类。
     * @return 返回查询结果的包装类 Result<PageResult>，其中 PageResult 为分页查询结果类。
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> page = setmealMapper.page(setmealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 根据id查询套餐
     *
     * @param id 套餐id
     * @return 返回查询到的套餐详情
     */
    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = new SetmealVO();

        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);

        List<SetmealDish> setmealDishes = setmealDishMapper.getByDishId(setmeal.getId());

        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }


    /**
     * 修改套餐
     *
     * @param setmealDTO 套餐信息
     * @return 返回操作结果，包含成功消息
     */
    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        setmealMapper.update(setmeal);

    /**
     * 根据状态对套餐进行起售/停售操作
     *
     * @param status 套餐状态：0-停售，1-起售
     * @param id 套餐id
     * @return 操作结果，成功返回{@link Result#success(String)}
     */
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        List<Long> ids = new ArrayList<>();
        ids.add(setmeal.getId());
        setmealDishMapper.deleteBySetmealIds(ids);

        if (setmealDishes != null && setmealDishes.size() > 0) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmeal.getId());
            });
        }

        setmealDishMapper.insertSetmealDishsBatch(setmealDishes);

    }



    /**
     * 批量删除指定ids的记录。
     *
     * @param ids 待删除记录的id列表。
     */
    @Override
    public void delete(List<Long> ids) {

        setmealMapper.deleteByIds(ids);

        setmealDishMapper.deleteBySetmealIds(ids);

    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.update(setmeal);
    }
}
