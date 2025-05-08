package com.pbdcompany.service;

import com.pbdcompany.dto.response.DeliveryStatus;
import com.pbdcompany.dto.response.TrackingResponse;
import com.pbdcompany.entity.Order;
import com.pbdcompany.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackingService {
    @Autowired
    private OrderRepository orderRepository;
    public TrackingResponse getTrackingInfo(String orderId)
    {
       Order order = orderRepository.findById(orderId)
               .orElseThrow(() -> new RuntimeException("Order not found"));

       return new TrackingResponse(
               order.getLogisticsCompany(),
               order.getTrackingNumber(),
               DeliveryStatus.valueOf(order.getDeliveryStatus()) //字符串转枚举
       );
    }
}
