package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类管理")
@Slf4j
public class CateGoryController {

    @Autowired
    private CategoryService cateGoryService;

    /**
     * 新增分类/套餐
     *
     * @param categoryDTO 分类/套餐信息
     * @return 返回操作结果，成功返回SUCCESS常量
     */
   @PostMapping
    @ApiOperation(value = "新增分类/套餐")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO) {

        log.info("新增分类/套餐:{}",categoryDTO);

        cateGoryService.insert(categoryDTO);

        return Result.success(MessageConstant.OPERATE_SUCCESS);

    }


    /**
     * 分页查询分类/套餐
     *
     * @param categoryPageQueryDTO 分页查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询分类/套餐")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {

        log.info("分页查询分类/套餐:{}",categoryPageQueryDTO);

        return Result.success(cateGoryService.page(categoryPageQueryDTO));

    }

    /**
     * 根据分类/套餐状态和id修改分类/套餐状态
     *
     * @param status 分类/套餐状态
     * @param id 分类/套餐id
     * @return 返回操作结果，成功返回success(消息常量：OPERATE_SUCCESS)
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "修改分类/套餐状态")
    public Result<String> updateStatus(@PathVariable("status") Integer status, Long id) {

        log.info("修改分类/套餐状态:{},{}",status,id);

        cateGoryService.startOrStop(status, id);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

    /**
     * 查询所有分类/套餐
     *
     * @param type 分类/套餐类型
     * @return 包含分类/套餐列表的Result对象
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有分类/套餐")
    public Result<List<Category>> list(Integer type) {

        log.info("查询所有分类/套餐:{}",type);

        return Result.success(cateGoryService.list(type));

    }

    /**
     * 使用PUT请求修改分类/套餐
     *
     * @param categoryDTO 包含分类/套餐信息的DTO对象
     * @return 返回操作结果，成功时为MessageConstant.OPERATE_SUCCESS
     */
    @PutMapping
    @ApiOperation(value = "修改分类/套餐")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {

        log.info("修改分类/套餐:{}",categoryDTO);

        cateGoryService.update(categoryDTO);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

    /**
     * 根据id删除分类/套餐
     *
     * @param id 分类/套餐id
     * @return 返回操作结果，成功返回MessageConstant.OPERATE_SUCCESS
     */
    @DeleteMapping
    @ApiOperation(value = "删除分类/套餐")
    public Result<String> delete(Long id) {

        log.info("删除分类/套餐:{}",id);

        cateGoryService.delete(id);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }
}
