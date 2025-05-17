package com.pbdcompany.controller;

import com.pbdcompany.dto.request.OrderRequest;
import com.pbdcompany.entity.Orders;
import com.pbdcompany.service.OrderService;
import com.pbdcompany.Utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/customer/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 假设你有一个 JWT 工具类来解析 Token
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(
            @RequestBody OrderRequest request,
            HttpServletRequest httpServletRequest) {

        // 1. 从请求头中获取 Token
        String token = parseJwt(httpServletRequest);

        if (token == null) {
            return ResponseEntity.status(401).body("Missing token");
        }

        // 2. 解析 Token 获取用户 ID
        Long customerId = getCustomerIdFromToken(token);

        if (customerId == null) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        // 3. 调用业务逻辑
        Orders orders = orderService.createOrder(customerId, request);
        return ResponseEntity.ok(orders);
    }

    // 从 Header 提取 JWT Token
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // 去除 "Bearer " 前缀
        }

        return null;
    }

    // 使用 JWT 解析出用户 ID（假设你有 JWT 工具类）
    private Long getCustomerIdFromToken(String token) {
        try {
            return JwtUtils.extractCustomerId(token); // 使用工具类方法
        } catch (Exception e) {
            return null;
        }
    }
}
