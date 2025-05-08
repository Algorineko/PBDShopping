package com.pbdcompany.service;

import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.dto.response.RegisterResponse;
import com.pbdcompany.entity.Customer;
import com.pbdcompany.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public RegisterResponse register(RegisterRequest request){
        if(customerRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Username is already exists");
        }

        Customer customer = new Customer();
        customer.setUsername(request.getUsername());
        customer.setPassword(request.getPassword());
        customer.setPhone(request.getPhone());

        customer = customerRepository.save(customer); //保存到数据库
        return new RegisterResponse(customer.getId(), customer.getUsername());
    }
}
