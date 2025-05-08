package com.pbdcompany.service;

import com.pbdcompany.entity.Customer;
import com.pbdcompany.entity.CustomerUserDetails;
import com.pbdcompany.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailService implements UserDetailsService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow( () -> new UsernameNotFoundException("Customer not found"));
        return new CustomerUserDetails(customer);
    }
}
