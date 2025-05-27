package com.pbdcompany.service.impl;

import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.dto.response.RegisterResponse;
import com.pbdcompany.entity.Customer;
import com.pbdcompany.mapper.CustomerMapper;
import com.pbdcompany.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        Customer customer = new Customer();
        customer.setCustomerName(request.getUsername());
        customer.setPassword(request.getPassword()); // 实际应加密存储
        customer.setPhoneNumber(request.getPhone());

        customerMapper.insert(customer);

        RegisterResponse response = new RegisterResponse();
        response.setId(customer.getCustomerId());
        response.setUsername(customer.getCustomerName());
        response.setSuccess(true);

        return response;
    }

    @Override
    public Customer login(String username, String password) {
        Customer customer = customerMapper.findByUsernameAndPassword(username,password);
        if (customer != null && customer.getPassword().equals(password)) {
            return customer;
        }
        return null;
    }
}
