package com.pbdcompany.controller;

import com.pbdcompany.dto.request.OrderRequest;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public OrderResponse createOrder(@RequestBody OrderRequest request)
    {
        return orderService.createOrder(request);
    }

    @PostMapping("/pay")
    public OrderResponse payOrder(@RequestBody String orderId, String paymentMethod){
        return orderService.payOrder(orderId, paymentMethod);
    }
}
