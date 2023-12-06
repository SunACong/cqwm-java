package com.sky.mapper;


import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDetailMapper {
    void insertBatch(List<OrderDetail> orderDetailList);

    List<OrderDetail> getByOrderId(Long id);

    @Delete("delete from order_detail where order_id = #{id}")
    void deleteByOrderId(Long id);

    @MapKey("order_id")
    List<Map<String, Object>> getSalesTop10(Map map);
}
