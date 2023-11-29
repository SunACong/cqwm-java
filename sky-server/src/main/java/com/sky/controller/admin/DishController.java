package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;


    /**
     * 新增菜品
     *
     * @param dishDTO 菜品信息
     * @return 操作结果
     */
    @PostMapping
    @ApiOperation(value = "新增菜品")
    public Result<String> insert(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品:{}" , dishDTO);

        dishService.insert(dishDTO);
        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }



    /**
     * 分页查询菜品
     * @param dishPageQueryDTO 菜品查询参数
     * @return 分页查询结果
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询菜品")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品:{}" , dishPageQueryDTO);

        PageResult page  = dishService.page(dishPageQueryDTO);
        return Result.success(page);
    }

        /**
     * 批量删除菜品
     *
     * @param ids 菜品ID数组
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("批量删除菜品:{}", ids);

        dishService.delete(ids);
        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

        /**
     * 根据ID查询菜品
     * @param id 菜品的ID
     * @return 菜品信息的Result对象
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询菜品")
    public Result<DishDTO> getById(@PathVariable Long id) {
        log.info("根据ID查询菜品:{}", id);

        DishDTO dishDTO = dishService.getById(id);

        return Result.success(dishDTO);
    }

        /**
     * 修改菜品
     *
     * @param dishDTO 菜品信息
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result<String> update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品:{}", dishDTO);

        dishService.update(dishDTO);
        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

        /**
     * 修改菜品状态
     *
     * @param status 状态值
     * @param id 菜品ID
     * @return 修改结果
     */
    @PostMapping("status/{status}")
    @ApiOperation("修改菜品状态")
    public Result<String> updateStatus(@PathVariable Integer status, Long id) {
        log.info("修改菜品状态:{}", id);

        dishService.startOrStop(status, id);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

    /**
     * 查询菜品列表
     *
     * @param categoryId 菜品分类ID
     * @return 返回包含所查询菜品列表的结果
     */
    @GetMapping("/list")
    @ApiOperation("查询菜品列表")
    public Result<List<Dish>> list(Long categoryId) {
        log.info("菜品分类ID：{}", categoryId);

        List<Dish> dishes = dishService.list(categoryId);
        return Result.success(dishes);
    }

}
