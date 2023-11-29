package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺管理")
@Slf4j
public class ShopController {

    final static String USER_SHOP_STATUS = "shop-status";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/status")
    @ApiOperation("用户端-查询店铺营业状态")
    public Result<Integer> getShopStatus() {

        Integer  status = (Integer)  redisTemplate.opsForValue().get(USER_SHOP_STATUS);
        if (status == null) {
            status = 1;
        }
        log.info("用户端-查询电铺营业状态：{}", status == 1 ? "营业中" : "已关闭");
        return Result.success(status);
    }

}
