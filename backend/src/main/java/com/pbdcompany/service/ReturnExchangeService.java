package com.pbdcompany.service;

import com.pbdcompany.dto.response.DeliveryStatus;
import com.pbdcompany.entity.Order;
import com.pbdcompany.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReturnExchangeService {
    @Autowired
    private OrderRepository orderRepository;

    private Order getOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if(!"DELIVERED".equals(order.getStatus())){
            throw new RuntimeException("Order must be delivered before return");
        }

        if(order.getIsReturn() || order.getIsExchange()){
            throw new RuntimeException("Return/Exchange already applied");
        }
        return order;
    }
    public void applyReturn(String orderId, String customerId, String returnReason,String logisticsCompany, String trackingNumber){
        Order order = getOrder(orderId);

        order.setIsReturn(true);
        order.setReturnReason(returnReason);
        order.setLogisticsCompany(logisticsCompany);
        order.setTrackingNumber(trackingNumber);
        order.setStatus("RETURNED");
    }

    public void applyExchange(String orderId, String customerId, String exchangeReason, String logisticsCompany, String trackingNumber) {
        Order order =  getOrder(orderId);
        order.setIsExchange(true);
        order.setReturnReason(exchangeReason);
        order.setLogisticsCompany(logisticsCompany);
        order.setTrackingNumber(trackingNumber);
        order.setStatus("EXCHANGE");
    }
}
