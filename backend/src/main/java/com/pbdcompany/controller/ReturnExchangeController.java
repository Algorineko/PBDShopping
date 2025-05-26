package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.ReturnExchangeRequest;
import com.pbdcompany.entity.ReturnExchange;
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

    //5.26修改：将 JwtUtils 改为可注入的 Bean。
    // 提取用户ID的通用方法
    private int extractCustomerId(HttpServletRequest request) {
        String token = parseJwt(request);
        if (token == null) {
            throw new RuntimeException("Missing or invalid token");
        }
        return jwtUtils.extractCustomerId(token);
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

        // 0. 通过Request得到实体类
        ReturnExchange returnExchange = returnExchangeService.getByRequest(request);

        // 1. 解析 Token 获取用户 ID
        int customerId = extractCustomerId(httpServletRequest);
        returnExchange.setCustomerId(customerId);

        // 2. 设置默认状态（可选）
        if (returnExchange.getStatus() == 0) {
            returnExchange.setStatus(0); // 默认为待处理
        }

        // 3. 插入退换货请求
        returnExchangeService.insert(returnExchange);

        return ResponseEntity.ok(request);
    }

}

