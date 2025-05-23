package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.ReturnExchangeRequest;
import com.pbdcompany.service.ReturnExchangeRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/return-exchange")
public class ReturnExchangeController {

    @Autowired
    private ReturnExchangeRequestService returnExchangeRequestService;

    @Autowired
    private JwtUtils jwtUtils; // 注入 JWT 工具类

    // 提取用户ID的通用方法
    private int extractCustomerId(HttpServletRequest request) {
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

    // Apply Return or Exchange
    @PostMapping("/{orderId}")
    public ResponseEntity<ReturnExchangeRequest> applyReturnExchange(
            @PathVariable int orderId,
            @RequestBody ReturnExchangeRequest request,
            HttpServletRequest httpServletRequest) {

        // 1. 解析 Token 获取用户 ID
        int customerId = extractCustomerId(httpServletRequest);
        request.setCustomerId(customerId);

        // 2. 设置默认状态（可选）
        if (request.getStatus() == 0) {
            request.setStatus(0); // 默认为待处理
        }

        // 3. 插入退换货请求
        returnExchangeRequestService.insert(request);

        return ResponseEntity.ok(request);
    }

}

