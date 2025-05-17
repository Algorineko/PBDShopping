package com.pbdcompany.service;

import com.pbdcompany.entity.Orders;
import com.pbdcompany.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReturnExchangeService {
    @Autowired
    private OrderRepository orderRepository;

    private Orders getOrder(Long orderId) {
        Orders orders = orderRepository.findById(String.valueOf(orderId))
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if(!"DELIVERED".equals(orders.getStatus())){
            throw new RuntimeException("Order must be delivered before return");
        }

        if(orders.getIsReturn() || orders.getIsExchange()){
            throw new RuntimeException("Return/Exchange already applied");
        }
        return orders;
    }
    public void applyReturn(Long orderId, Long customerId, String returnReason,String logisticsCompany, String trackingNumber){
        Orders orders = getOrder(Long.valueOf(orderId));

        orders.setIsReturn(true);
        orders.setReturnReason(returnReason);
        orders.setLogisticsCompany(logisticsCompany);
        orders.setTrackingNumber(trackingNumber);
        orders.setStatus("RETURNED");
    }

    public void applyExchange(Long orderId, Long customerId, String exchangeReason, String logisticsCompany, String trackingNumber) {
        Orders orders =  getOrder(Long.valueOf(orderId));
        orders.setIsExchange(true);
        orders.setReturnReason(exchangeReason);
        orders.setLogisticsCompany(logisticsCompany);
        orders.setTrackingNumber(trackingNumber);
        orders.setStatus("EXCHANGE");
    }
}
