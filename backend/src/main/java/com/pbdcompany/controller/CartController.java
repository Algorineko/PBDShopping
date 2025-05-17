package com.pbdcompany.controller;

import com.pbdcompany.dto.request.AddToCartRequest;
import com.pbdcompany.dto.response.CartResponse;
import com.pbdcompany.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pbdcompany.Utils.JwtUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/cart")
public class CartController {

    private final CartService cartService;
    private final JwtUtils jwtUtils;

    public CartController(CartService cartService, JwtUtils jwtUtils) {
        this.cartService = cartService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody AddToCartRequest request,
            HttpServletRequest httpServletRequest) {

        Long customerId = extractCustomerIdFromToken(httpServletRequest);
        cartService.addToCart(customerId, request);

        return ResponseEntity.ok("Item added to cart successfully");
    }

    @GetMapping  ("/get")
    public ResponseEntity<?> getCart(HttpServletRequest httpServletRequest) {
        Long customerId = extractCustomerIdFromToken(httpServletRequest);
        CartResponse cartResponse = cartService.getCart(customerId);
        return ResponseEntity.ok(cartResponse);
    }

    // 从请求中提取用户ID
    private Long extractCustomerIdFromToken(HttpServletRequest request) {
        String token = parseJwt(request);
        if (token == null) {
            throw new RuntimeException("Missing or invalid token");
        }
        return jwtUtils.extractCustomerId(token);
    }

    // 从 Header 提取 JWT Token
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // 去除 "Bearer " 前缀
        }

        return null;
    }
}
