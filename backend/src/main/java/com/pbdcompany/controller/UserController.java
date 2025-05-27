package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.LoginRequest;
import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.entity.Customer;
import com.pbdcompany.entity.Merchant;
import com.pbdcompany.service.CustomerService;
import com.pbdcompany.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MerchantService merchantService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if ("customer".equalsIgnoreCase(String.valueOf(request.getUserType()))) {
            return ResponseEntity.ok(customerService.register(request));
        } else if ("merchant".equalsIgnoreCase(String.valueOf(request.getUserType()))) {
            return ResponseEntity.ok(merchantService.register(request));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("不支持的用户类型");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String userType = String.valueOf(request.getUserType());
        Object user = null;

        if ("customer".equalsIgnoreCase(userType)) {
            user = customerService.login(request.getUsername(), request.getPassword());
        } else if ("merchant".equalsIgnoreCase(userType)) {
            user = merchantService.login(request.getUsername(), request.getPassword());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("不支持的用户类型");
        }

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        }

        // 生成 Token 并返回给前端
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userType", userType);

        if ("customer".equalsIgnoreCase(userType)) {
            Customer customer = (Customer) user;
            extraClaims.put("customerId", customer.getCustomerId());
        } else if ("merchant".equalsIgnoreCase(userType)) {
            Merchant merchant = (Merchant) user;
            extraClaims.put("merchantId", merchant.getMerchantId());
        }

        String token = JwtUtils.generateToken(extraClaims, request.getUsername());

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(Map.of(
                        "token", token,
                        "user", user));
    }
}
