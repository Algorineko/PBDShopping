package com.pbdcompany.controller;

import com.pbdcompany.dto.request.OrderRequest;
import com.pbdcompany.dto.response.OrderInfoResponse;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.service.OrdersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/customer/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest request,
            HttpServletRequest httpServletRequest) {

        String token = parseJwt(httpServletRequest);
        Integer userId = (Integer) getCustomerIdFromToken(token);

        if (token == null || userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        OrderResponse response = ordersService.createOrder(userId, request);
        return ResponseEntity.ok(response);
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
    private Object getCustomerIdFromToken(String token) {
        try {
            return JwtUtils.extractCustomerId(token); // 使用工具类方法
        } catch (Exception e) {
            return null;
        }
    }

    @Autowired


    // 查看商家所有订单
    @GetMapping("/list")
    public List<OrderInfoResponse> getOrdersByMerchant(@RequestParam int merchantId) {
        return ordersService.getOrdersByMerchantId(merchantId);
    }

    // 处理订单：发货 / 退货 / 换货
    @PutMapping("/update")
    public String handleOrder(@RequestBody UpdateOrderRequest request) {
        if (ordersService.updateOrder(request)) {
            return "订单更新成功";
        } else {
            return "订单不存在或无权限操作";
        }
    }
}
