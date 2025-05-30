package com.pbdcompany.controller;

import com.pbdcompany.dto.request.OrderCreateRequest;
import com.pbdcompany.dto.request.OrderRequest;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 查询用户的所有订单
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable int customerId) {
        List<OrderResponse> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    // 查询商家的所有订单
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByMerchantId(@PathVariable int merchantId) {
        List<OrderResponse> orders = orderService.getOrdersByMerchantId(merchantId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createOrder(@RequestBody OrderCreateRequest request) {
        boolean success = orderService.createOrder(request);
        return ResponseEntity.ok(success);
    }
}
