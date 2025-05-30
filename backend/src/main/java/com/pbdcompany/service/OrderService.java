package com.pbdcompany.service;

import com.pbdcompany.dto.request.OrderCreateRequest;
import com.pbdcompany.dto.response.OrderItemResponse;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.entity.OrderItem;
import com.pbdcompany.entity.Orders;
import com.pbdcompany.enums.Status;
import com.pbdcompany.mapper.OrderItemMapper;
import com.pbdcompany.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    // 获取用户的所有订单（含订单项）
    public List<OrderResponse> getOrdersByCustomerId(int customerId) {
        List<Orders> orders = orderMapper.findByCustomerId(customerId);
        return convertToResponses(orders);
    }

    // 获取商户的所有订单（含订单项）
    public List<OrderResponse> getOrdersByMerchantId(int merchantId) {
        List<Orders> orders = orderMapper.findByMerchantId(merchantId);
        return convertToResponses(orders);
    }

    // 转换实体类为响应对象
    private List<OrderResponse> convertToResponses(List<Orders> ordersList) {
        return ordersList.stream()
                .map(order -> {
                    OrderResponse response = new OrderResponse();
                    response.setOrderId(order.getOrderId());
                    response.setCustomerId(order.getCustomerId());
                    response.setMerchantId(order.getMerchantId());
                    response.setTotalPrice(order.getTotalPrice());
                    response.setStatus(order.getStatus().name());

                    // 查询订单项
                    List<OrderItem> items = orderItemMapper.findByOrderId(order.getOrderId());
                    System.out.println(items);
                    System.out.println(order.getOrderId());
                    List<OrderItemResponse> itemResponses = items.stream()
                            .map(item -> {
                                OrderItemResponse res = new OrderItemResponse();
                                res.setOrderItemId(item.getOrderItemId());
                                res.setProductId(item.getProductId());
                                res.setQuantity(item.getQuantity());
                                res.setPrice(item.getPrice());
                                return res;
                            })
                            .collect(Collectors.toList());

                    response.setItems(itemResponses);
                    return response;
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public boolean createOrder(OrderCreateRequest request) {
        try {
            // 1. 生成订单号（可使用时间戳 + 随机数）
            // 这里简化为数据库自增主键

            // 2. 计算总价
            double totalPrice = request.getItems().stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();

            // 3. 构建订单对象
            Orders order = new Orders();
            order.setCustomerId(request.getUserId());
            order.setMerchantId(request.getMerchantId());
            order.setTotalPrice(totalPrice);
            order.setStatus(Status.PENDING); // 默认状态

            // 4. 插入订单
            orderMapper.insert(order);

            // 5. 构建订单项列表
            List<OrderItem> items = request.getItems().stream()
                    .map(item -> {
                        OrderItem oi = new OrderItem();
                        oi.setOrderId(order.getOrderId()); // 获取自动生成的订单ID
                        oi.setProductId(item.getProductId());
                        oi.setQuantity(item.getQuantity());
                        oi.setPrice(item.getPrice());
                        return oi;
                    })
                    .collect(Collectors.toList());

            // 6. 批量插入订单项
            if (!items.isEmpty()) {
                orderItemMapper.insertBatch(items);
            }

            return true;

        } catch (Exception e) {
            // 日志记录异常信息
            e.printStackTrace();
            return false;
        }
    }
}
