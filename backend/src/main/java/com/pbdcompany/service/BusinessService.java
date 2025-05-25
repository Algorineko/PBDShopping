package com.pbdcompany.service;

import com.pbdcompany.entity.Business;
import com.pbdcompany.mapper.BusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BusinessService {

    @Autowired
    private BusinessMapper businessMapper;

    public Business register(Business business) {
        businessMapper.insert(business);
        return business;
    }

    public Business login(String username, String password) {
        Business b = businessMapper.selectByUsername(username);
        if (b != null && b.getPassword().equals(password)) {
            return b;
        }
        return null;
    }
}
