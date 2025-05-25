package com.pbdcompany.controller;

import com.pbdcompany.dto.request.BusinessLoginRequest;
import com.pbdcompany.dto.request.BusinessRegisterRequest;
import com.pbdcompany.entity.Business;
import com.pbdcompany.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @PostMapping("/register")
    public ResponseEntity<Business> register(@RequestBody BusinessRegisterRequest businessRegisterRequest) {
        Business business = new Business();
        business.setUsername(businessRegisterRequest.getUsername());
        business.setPassword(businessRegisterRequest.getPassword());
        business.setShopName(businessRegisterRequest.getShopName());
        business.setContactPhone(businessRegisterRequest.getContactPhone());
        business.setVerificationCode(businessRegisterRequest.getVerificationCode());
        return ResponseEntity.ok(businessService.register(business));
    }

    @PostMapping("/login")
    public ResponseEntity<Business> login(@RequestBody BusinessLoginRequest businessLoginRequest) {
        String username = businessLoginRequest.getUsername();
        String password = businessLoginRequest.getPassword();
        Business b = businessService.login(username, password);
        if (b == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(b);
    }
}
