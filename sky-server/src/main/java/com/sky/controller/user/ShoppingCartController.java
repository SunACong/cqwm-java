package com.sky.controller.user;


import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user/shoppingCart")
@Api(tags = "小程序-购物车接口")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO 购物车对象
     * @return 返回操作结果
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加购物车", notes = "添加购物车", httpMethod = "POST")
    public Result<String> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车：{}", shoppingCartDTO);

        shoppingCartService.add(shoppingCartDTO);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

    /**
     * 查询购物车列表
     *
     * @return 返回购物车列表
     */
    @GetMapping("/list")
    @ApiOperation("查询购物车列表")
    public Result<List<ShoppingCart>> lsit() {
        log.info("查询购物车列表：{}", BaseContext.getCurrentId());

        return Result.success(shoppingCartService.lsit());
    }


    /**
     * 清空购物车
     *
     * @return 返回操作结果，成功时包含清空成功的消息
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result<String> clean() {
        log.info("清空购物车：{}", BaseContext.getCurrentId());

        shoppingCartService.clean();

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

    /**
     * 从购物车中删除商品
     *
     * @param shoppingCartDTO 购物车商品信息
     * @return 操作结果，成功返回SUCCESS，失败返回失败信息
     */
    @PostMapping("/sub")
    @ApiOperation("删除购物车中一个商品")
    public Result<String> sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除购物车中一个商品：{}", shoppingCartDTO);

        shoppingCartService.sub(shoppingCartDTO);

        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

}
