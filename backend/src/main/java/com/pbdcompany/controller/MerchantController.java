
package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.ChangePasswordRequest;
import com.pbdcompany.dto.request.UpdateMerchantProfileRequest;
import com.pbdcompany.dto.response.MerchantProfileResponse;
import com.pbdcompany.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private JwtUtils jwtUtils;

    // 获取当前商家信息
    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentMerchantProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        System.out.println("authHeader" + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }

        String token = authHeader.substring(7);
        String username = jwtUtils.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无效的 Token");
        }

        MerchantProfileResponse profile = merchantService.getMerchantProfileByUsername(username);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("商家不存在");
        }

        return ResponseEntity.ok(profile);
    }

    // 更新当前商家信息
    @PutMapping("/putProfile")
    public ResponseEntity<?> updateCurrentMerchantProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UpdateMerchantProfileRequest request) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }

        String token = authHeader.substring(7);
        String username = jwtUtils.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无效的 Token");
        }

        boolean success = merchantService.updateMerchantProfile(username, request);
        if (!success) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失败");
        }

        return ResponseEntity.ok("信息更新成功");
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changeMerchantPassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ChangePasswordRequest passwordRequest) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }

        String token = authHeader.substring(7);
        String username = jwtUtils.getUsernameFromToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无效的 Token");
        }

        String oldPassword = passwordRequest.getOldPassword();
        String newPassword = passwordRequest.getNewPassword();

        boolean success = merchantService.changePassword(username, oldPassword, newPassword);
        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("密码修改失败，请检查旧密码是否正确");
        }

        return ResponseEntity.ok("密码修改成功");
    }

}
