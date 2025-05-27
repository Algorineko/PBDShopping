package com.pbdcompany.controller;


import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.UpdateCustomerProfileRequest;
import com.pbdcompany.dto.response.CustomerProfileResponse;
import com.pbdcompany.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *  CustomerController
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private JwtUtils jwtUtils;

    // 获取当前用户信息
    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUserProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        System.out.println("AuthHeader: " + authHeader); // 调试输出

        if (authHeader==null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }

        String token = authHeader.substring(7);
        System.out.println("Token: " + token); // 输出 Token

        String username = jwtUtils.getUsernameFromToken(token);
        System.out.println("Username from token: " + username); // 输出用户名

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无效的 Token");
        }

        CustomerProfileResponse profile = customerService.getCustomerProfileByUsername(username);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户不存在");
        }

        return ResponseEntity.ok(profile);
    }


    @PutMapping("/putProfile")
    public ResponseEntity<?> updateCurrentUserProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UpdateCustomerProfileRequest request) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }

        String token = authHeader.substring(7);
        String username = jwtUtils.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无效的 Token");
        }

        boolean success = customerService.updateCustomerProfile(username, request);
        if (!success) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失败");
        }

        return ResponseEntity.ok("信息更新成功");
    }


}


