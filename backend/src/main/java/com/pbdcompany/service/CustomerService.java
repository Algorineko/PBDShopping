package com.pbdcompany.service;

import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.dto.request.UpdateCustomerProfileRequest;
import com.pbdcompany.dto.response.CustomerProfileResponse;
import com.pbdcompany.dto.response.RegisterResponse;
import com.pbdcompany.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    RegisterResponse register(RegisterRequest request);

    Customer login(String username, String password);

    CustomerProfileResponse getCustomerProfileByUsername(String username);

    boolean updateCustomerProfile(String username, UpdateCustomerProfileRequest request);

    boolean existsByUsername(String username);
}

