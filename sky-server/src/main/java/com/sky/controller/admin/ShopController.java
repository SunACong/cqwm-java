package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺管理")
@Slf4j
public class ShopController {

    final static String ADMIN_SHOP_STATUS = "shop-status";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

        /**
     * 切换店铺营业状态
     * @param status 营业状态
     * @return 操作结果
     */
    @PutMapping("/{status}")
    @ApiOperation("管理端-切换店铺营业状态")
    public Result<String> setShopStatus(@PathVariable Integer status) {
        log.info("切换店铺营业状态: {}", status == 1? "营业中" : "已关闭");

        redisTemplate.opsForValue().set(ADMIN_SHOP_STATUS, status);
        return Result.success(MessageConstant.OPERATE_SUCCESS);
    }

    @GetMapping("/status")
    @ApiOperation("管理端-查询店铺营业状态")
    public Result<Integer> getShopStatus() {

        Integer  status = (Integer)  redisTemplate.opsForValue().get(ADMIN_SHOP_STATUS);
        if (status == null) {
            status = 1;
        }
        log.info("查询电铺营业状态：{}", status == 1 ? "营业中" : "已关闭");
        return Result.success(status);
    }

}
