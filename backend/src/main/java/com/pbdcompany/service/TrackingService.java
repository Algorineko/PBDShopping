package com.pbdcompany.service;

import com.pbdcompany.dto.response.DeliveryStatus;
import com.pbdcompany.dto.response.TrackingResponse;
import com.pbdcompany.entity.Orders;
import com.pbdcompany.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackingService {
    @Autowired
    private OrderRepository orderRepository;
    public TrackingResponse getTrackingInfo(Long orderId)
    {
       Orders orders = orderRepository.findById(orderId)
               .orElseThrow(() -> new RuntimeException("Order not found"));

       return new TrackingResponse(
               orders.getLogisticsCompany(),
               orders.getTrackingNumber(),
               DeliveryStatus.valueOf(orders.getDeliveryStatus()) //字符串转枚举
       );
    }
}
