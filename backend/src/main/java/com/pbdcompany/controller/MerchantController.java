package com.pbdcompany.controller;

import com.pbdcompany.dto.request.MerchantRegisterRequest;
import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.dto.response.MerchantResponse;
import com.pbdcompany.entity.Merchant;
import com.pbdcompany.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;


}
