
package com.pbdcompany.service;

import org.example.springbootmybatis.entity.Orders;
import org.example.springbootmybatis.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    // 查询所有订单
    public List<Orders> findAll() {
        return ordersMapper.findAll();
    }

    // 根据ID删除订单
    public void deleteById(int id) {
        ordersMapper.deleteById(id);
    }

    // 添加订单
    public void insert(Orders orders) {
        ordersMapper.insert(orders);
    }

    // 更新订单信息
    public void update(Orders orders) {
        ordersMapper.update(orders);
    }

    // 根据ID查询订单信息
    public Orders findById(int id) {
        return ordersMapper.findById(id);
    }
}