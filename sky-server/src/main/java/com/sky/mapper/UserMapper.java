package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;

@Mapper
public interface UserMapper {
    @Select(value = "select * from user where openid = #{openid}")
    User selectByOpenid(String openid);

    void insert(User user);


    @Select(value = "select * from user where id = #{userId}")
    User getById(Long userId);

    Integer userStatisticsByMap(HashMap map);
}
