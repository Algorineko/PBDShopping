package com.pbdcompany.controller;

import com.pbdcompany.dto.request.OrderRequest;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.entity.CustomerUserDetails;
import com.pbdcompany.entity.Order;
import com.pbdcompany.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(
            @RequestBody OrderRequest request,
            @AuthenticationPrincipal CustomerUserDetails userDetails) {
        String customerId = userDetails.getCustomerId();
        Order order = orderService.createOrder(customerId, request);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/pay")
    public OrderResponse payOrder(@RequestBody String orderId, String paymentMethod){
        return orderService.payOrder(orderId, paymentMethod);
    }
}
