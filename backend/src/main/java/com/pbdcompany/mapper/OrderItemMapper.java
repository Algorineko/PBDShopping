package com.pbdcompany.mapper;

import com.pbdcompany.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper {


    void insertBatch(@Param("items") List<OrderItem> items);

    List<OrderItem> findByOrderId(int orderId);

    List<OrderItem> findAll();

}

