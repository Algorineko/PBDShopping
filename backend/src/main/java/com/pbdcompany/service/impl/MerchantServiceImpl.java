package com.pbdcompany.service.impl;

import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.dto.response.RegisterResponse;
import com.pbdcompany.entity.Merchant;
import com.pbdcompany.mapper.MerchantMapper;
import com.pbdcompany.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        Merchant merchant = new Merchant();
        merchant.setMerchantName(request.getUsername());
        merchant.setPassword(request.getPassword());
        merchant.setPhoneNumber(request.getPhone());

        merchantMapper.insert(merchant);

        RegisterResponse response = new RegisterResponse();
        response.setId(merchant.getMerchantId());
        response.setUsername(merchant.getMerchantName());
        response.setSuccess(true);
        return response;
    }

    @Override
    public Merchant login(String username, String password) {
        Merchant merchant = merchantMapper.findByMerchantName(username);;
        if (merchant != null && merchant.getPassword().equals(password)) {
            return merchant;
        }
        return null;
    }
}

