package com.pbdcompany.mapper;

import com.pbdcompany.dto.response.OrderItemResponse;
import com.pbdcompany.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean
public interface OrderItemMapper {

    public List<OrderItem> findAll();

    public void deleteById(int id);

    public int insert(OrderItem OrderItem);

    public void update(OrderItem OrderItem);

    public OrderItem findById(int id);

    public List<OrderItemResponse> findByOrderId(int orderId);



}
