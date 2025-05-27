package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.LoginRequest;
import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.entity.Admin;
import com.pbdcompany.entity.Customer;
import com.pbdcompany.entity.Merchant;
import com.pbdcompany.service.AdminService;
import com.pbdcompany.service.CustomerService;
import com.pbdcompany.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String userType = String.valueOf(request.getUserType());

        if ("customer".equalsIgnoreCase(userType)) {
            if (customerService.existsByUsername(request.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("该用户名已被注册");
            }
            return ResponseEntity.ok(customerService.register(request));
        } else if ("merchant".equalsIgnoreCase(userType)) {
            if (merchantService.existsByUsername(request.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("该用户名已被注册");
            }
            return ResponseEntity.ok(merchantService.register(request));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("不支持的用户类型");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String userType = request.getUserType();
        String username = request.getUsername();
        String password = request.getPassword();

        if (username == null || password == null || userType == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户名、密码或用户类型不能为空");
        }

        Object user = null;

        if ("customer".equalsIgnoreCase(userType)) {
            user = customerService.login(username, password);
        } else if ("merchant".equalsIgnoreCase(userType)) {
            user = merchantService.login(username, password);
        } else if ("admin".equalsIgnoreCase(userType)) {
            List<Admin> admins = adminService.getAllAdmins();
            for (Admin admin : admins) {
                if (Objects.equals(admin.getAdminName(), username) &&
                        Objects.equals(admin.getPassword(), password)) {
                    user = admin;
                    break;
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("不支持的用户类型");
        }

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        }

        Map<String, Object> extraClaims = getExtraClaims(userType, user);
        if (extraClaims == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("生成 Token 参数失败");
        }

        String token = null;
        try {
            token = jwtUtils.generateToken(extraClaims, username);
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常堆栈
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("生成 Token 失败: " + e.getMessage());
        }

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(Map.of(
                        "token", token,
                        "user", user));
    }


    private static Map<String, Object> getExtraClaims(String userType, Object user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userType", userType);

        if ("customer".equalsIgnoreCase(userType)) {
            Customer customer = (Customer) user;
            extraClaims.put("customerId", customer.getCustomerId());
        } else if ("merchant".equalsIgnoreCase(userType)) {
            Merchant merchant = (Merchant) user;
            extraClaims.put("merchantId", merchant.getMerchantId());
        } else if ("admin".equalsIgnoreCase(userType)) {
            Admin admin = (Admin) user;
            extraClaims.put("adminId", admin.getAdminId());
        }
        return extraClaims;
    }

}
