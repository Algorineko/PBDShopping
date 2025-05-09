package com.pbdcompany.service;

import com.pbdcompany.dto.request.OrderRequest;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.entity.Order;
import com.pbdcompany.entity.Product;
import com.pbdcompany.repository.OrderRepository;
import com.pbdcompany.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(String customerId,OrderRequest request){
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(product.getPrice() * request.getQuantity());
        order.setStatus("PENDING"); //提交成功 订单待支付

        return order = orderRepository.save(order);

    }

    public OrderResponse payOrder(String orderId, String paymentMethod){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if(!"PENDING".equals(order.getStatus())){
            throw new RuntimeException("Order cannot be paid");
        }

        //TODO 支付逻辑有缺陷 需要修改
        order.setStatus("PAID");
        orderRepository.save(order);
        return new OrderResponse(
                order.getId(),order.getStatus(),order.getTotalPrice()
        );
    }
}
