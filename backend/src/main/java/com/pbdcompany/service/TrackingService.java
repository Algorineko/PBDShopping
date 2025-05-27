package com.pbdcompany.service;

import com.pbdcompany.dto.response.OrderItemTrackingResponse;
import com.pbdcompany.dto.response.OrderTrackingResponse;
import com.pbdcompany.entity.Logisticsinfo;
import com.pbdcompany.entity.OrderItem;
import com.pbdcompany.entity.Orders;
import com.pbdcompany.mapper.LogisticsinfoMapper;
import com.pbdcompany.mapper.OrderItemMapper;
import com.pbdcompany.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.pbdcompany.enums.Status;
@Service
public class TrackingService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private LogisticsinfoMapper logisticsinfoMapper;

    public OrderTrackingResponse getTrackingInfo(int orderId) {
        Orders order = ordersMapper.findById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        List<OrderItem> orderItems = orderItemMapper.findByOrderId(orderId);

        List<OrderItemTrackingResponse> itemResponses = orderItems.stream()
                .map(item -> {
                    Logisticsinfo logistics = logisticsinfoMapper.findByOrderItemId(item.getOrderItemId());

                    String upperCase = logistics.getStatus().toString().toUpperCase();
                    return new OrderItemTrackingResponse(
                            item.getOrderItemId(),
                            item.getProductId(),
                            item.getQuantity(),
                            logistics.getLogisticsCompany(),
                            logistics.getTrackingNumber(),
                            Status.valueOf(logistics.getStatus().toString().toUpperCase())
                    );
                })
                .collect(Collectors.toList());

        return new OrderTrackingResponse(orderId, itemResponses);
    }
}
