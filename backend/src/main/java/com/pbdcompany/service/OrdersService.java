
package com.pbdcompany.service;


import com.pbdcompany.dto.request.OrderRequest;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.entity.OrderItem;
import com.pbdcompany.entity.Orders;
import com.pbdcompany.mapper.OrderItemMapper;
import com.pbdcompany.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    // 查询所有订单
    public List<Orders> findAll() {
        return ordersMapper.findAll();
    }

    // 根据ID删除订单
    public void deleteById(int id) {
        ordersMapper.deleteById(id);
    }

    // 添加订单
    public int insert(Orders orders) {
        return ordersMapper.insert(orders);
    }

    // 更新订单信息
    public void update(Orders orders) {
        ordersMapper.update(orders);
    }

    // 根据ID查询订单信息
    public Orders findById(int id) {
        return ordersMapper.findById(id);
    }

    public OrderResponse createOrder(int customerId, OrderRequest request) {
        // 1. 创建订单主表
        Orders order = new Orders();
        order.setCustomerId(customerId);
        order.setTotalPrice(request.getTotalPrice());
        order.setStatus("PENDING"); // 初始状态为待支付


        //此处需要修改逻辑！！！需要将insert的orderId返回！！！

        int orderId = ordersMapper.insert(order);

        // 获取刚插入的订单ID

        // 2. 插入订单项
        for (OrderRequest.OrderItemRequest item : request.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice()); // 前端传入或后端计算均可

            //修改了此处的逻辑，引入了orderItemMapper
            orderItemMapper.insert(orderItem);
        }

        // 3. 返回响应
        return new OrderResponse(orderId, "PENDING", request.getTotalPrice());
    }

}