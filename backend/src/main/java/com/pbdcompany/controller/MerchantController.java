package com.pbdcompany.controller;

import com.pbdcompany.dto.request.MerchantRegisterRequest;
import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.dto.response.MerchantResponse;
import com.pbdcompany.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    // 注册商家
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        if (merchantService.register(request).getSuccess()) {
            return "注册成功";
        } else {
            return "用户名或手机号已存在";
        }
    }

    // 商家登录
    @PostMapping("/login")
    public MerchantResponse login(@RequestParam String merchantName, @RequestParam String password) {
        return merchantService.login(merchantName, password);
    }

    // 获取所有商家
    @GetMapping
    public List<MerchantResponse> getAll() {
        return merchantService.getAllMerchants();
    }

    // 根据 ID 获取商家
    @GetMapping("/{id}")
    public MerchantResponse getById(@PathVariable int id) {
        return merchantService.getMerchantById(id);
    }
}
