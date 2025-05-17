package com.pbdcompany.service;

import com.pbdcompany.dto.request.OrderRequest;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.entity.Customer;
import com.pbdcompany.entity.Orders;
import com.pbdcompany.entity.Product;
import com.pbdcompany.repository.OrderRepository;
import com.pbdcompany.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Orders createOrder(Long customerId, OrderRequest request){
        Product product = productRepository.findById(String.valueOf(request.getProductId()))
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Orders orders = new Orders();
        orders.setCustomerId(customerId);
        orders.setProductId(request.getProductId());
        orders.setQuantity(request.getQuantity());
        orders.setTotalPrice(product.getPrice() * request.getQuantity());
        orders.setStatus("PENDING"); //提交成功 订单待支付

        return orders = orderRepository.save(orders);

    }

    public OrderResponse payOrder(String orderId, String paymentMethod){
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if(!"PENDING".equals(orders.getStatus())){
            throw new RuntimeException("Order cannot be paid");
        }

        //TODO 支付逻辑有缺陷 需要修改
        orders.setStatus("PAID");
        orderRepository.save(orders);
        return new OrderResponse(
                orders.getId(), orders.getStatus(), orders.getTotalPrice()
        );
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }
}
