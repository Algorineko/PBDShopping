package com.pbdcompany.controller;

import com.pbdcompany.dto.request.OrderRequest;
import com.pbdcompany.dto.request.UpdateOrderRequest;
import com.pbdcompany.dto.response.OrderInfoResponse;
import com.pbdcompany.dto.response.OrderItemResponse;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.entity.Merchant;
import com.pbdcompany.service.OrderItemService;
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
    private OrderItemService orderItemService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private JwtUtils jwtUtils;
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

    @GetMapping("/items")
    public ResponseEntity<?> getOrderItemsByOrderId(@RequestParam int orderId,
                                                    HttpServletRequest request) {
        String token = parseJwt(request);
        Integer userId = (Integer) getCustomerIdFromToken(token);

        if (token == null || userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未授权");
        }

        // 检查订单是否属于当前用户（可选增强安全性）
        if (!ordersService.isOrderBelongsToUser(orderId, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("无权限访问此订单");
        }

        // 获取订单项
        List<OrderItemResponse> items = orderItemService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(items);
    }


    // 从 Header 提取 JWT Token
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // 去除 "Bearer " 前缀
        }

        return null;
    }


    private Object getCustomerIdFromToken(String token) {
        try {
            return jwtUtils.extractCustomerId(token); // 使用工具类方法
        } catch (Exception e) {
            return null;
        }
    }


    @GetMapping("/list")
    public List<OrderInfoResponse> getOrdersByMerchantId(@RequestParam int merchantId) {
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
