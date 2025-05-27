package com.pbdcompany.service.impl;

import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.dto.request.UpdateMerchantProfileRequest;
import com.pbdcompany.dto.response.MerchantProfileResponse;
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
        merchant.setPassword(request.getPassword()); // 实际应加密存储
        merchant.setPhoneNumber(request.getPhone());
        merchant.setMerchantAddress(request.getAddress());

        merchantMapper.insert(merchant);

        RegisterResponse response = new RegisterResponse();
        response.setId(merchant.getMerchantId());
        response.setUsername(merchant.getMerchantName());
        response.setSuccess(true);

        return response;
    }

    @Override
    public Merchant login(String username, String password) {
        Merchant merchant = merchantMapper.findByUsernameAndPassword(username, password);
        if (merchant != null && merchant.getPassword().equals(password)) {
            return merchant;
        }
        return null;
    }

    @Override
    public MerchantProfileResponse getMerchantProfileByUsername(String username) {
        return merchantMapper.findProfileByUsername(username);
    }

    @Override
    public boolean updateMerchantProfile(String username, UpdateMerchantProfileRequest request) {
        Merchant merchant = merchantMapper.findByUsername(username);
        if (merchant == null) {
            return false;
        }
        if (request.getPhoneNumber() != null) {
            merchant.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            merchant.setMerchantAddress(request.getAddress());
        }
        merchantMapper.update(merchant);
        return true;
    }

    @Override
    public boolean existsByUsername(String username) {
        return merchantMapper.findByUsername(username) != null;
    }
}
