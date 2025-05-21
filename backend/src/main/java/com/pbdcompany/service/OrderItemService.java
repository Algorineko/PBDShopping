
package com.pbdcompany.service;

import org.example.springbootmybatis.entity.OrderItem;
import org.example.springbootmybatis.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    // 查询所有订单项
    public List<OrderItem> findAll() {
        return orderItemMapper.findAll();
    }

    // 根据ID删除订单项
    public void deleteById(int id) {
        orderItemMapper.deleteById(id);
    }

    // 添加订单项
    public void insert(OrderItem orderItem) {
        orderItemMapper.insert(orderItem);
    }

    // 更新订单项信息
    public void update(OrderItem orderItem) {
        orderItemMapper.update(orderItem);
    }

    // 根据ID查询订单项信息
    public OrderItem findById(int id) {
        return orderItemMapper.findById(id);
    }
}