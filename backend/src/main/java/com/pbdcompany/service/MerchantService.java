package com.pbdcompany.service;

import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.dto.response.RegisterResponse;
import com.pbdcompany.entity.Merchant;
import org.springframework.stereotype.Service;

@Service
public interface MerchantService {
    RegisterResponse register(RegisterRequest request);
    Merchant login(String username, String password);
}
