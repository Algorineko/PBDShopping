package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.service.ReturnExchangeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/return-exchange")
public class ReturnExchangeController {

    @Autowired
    private ReturnExchangeService returnExchangeService;

    @Autowired
    private JwtUtils jwtUtils; // 注入 JWT 工具类

    // 提取用户ID的通用方法
    private Long extractCustomerId(HttpServletRequest request) {
        String token = parseJwt(request);
        if (token == null) {
            throw new RuntimeException("Missing or invalid token");
        }
        return JwtUtils.extractCustomerId(token);
    }

    // 从请求头提取 Token
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // 去除 "Bearer " 前缀
        }
        return null;
    }

    // Apply Return
    @PostMapping("/{orderId}/return")
    public ResponseEntity<?> applyReturn(
            @PathVariable Long orderId,
            @RequestParam String reason,
            @RequestParam String logisticsCompany,
            @RequestParam String trackingNumber,
            HttpServletRequest request) {

        Long customerId = extractCustomerId(request); // 使用 Token 获取用户 ID
        returnExchangeService.applyReturn(orderId, customerId, reason, logisticsCompany, trackingNumber);

        return ResponseEntity.ok("Return request submitted successfully");
    }

    // Apply Exchange
    @PostMapping("/{orderId}/exchange")
    public ResponseEntity<?> applyExchange(
            @PathVariable Long orderId,
            @RequestParam String reason,
            @RequestParam String logisticsCompany,
            @RequestParam String trackingNumber,
            HttpServletRequest request) {

        Long customerId = extractCustomerId(request); // 使用 Token 获取用户 ID
        returnExchangeService.applyExchange(orderId, customerId, reason, logisticsCompany, trackingNumber);

        return ResponseEntity.ok("Exchange request submitted successfully");
    }
}
