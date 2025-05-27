package com.pbdcompany.controller;


import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.dto.response.RegisterResponse;
import com.pbdcompany.entity.Customer;
import com.pbdcompany.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 *  CustomerController
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("/register")
    public RegisterResponse register(RegisterRequest request) {
        return customerService.register(request);
    }

    @RequestMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        Customer customer = customerService.login(username, password);
        if (customer == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .contentType(APPLICATION_JSON)
                    .body("\"用户名或密码错误\"");
            //5.26修改：增加了.contentType的属性设置，以避免在测试时返回的是乱码。
        }
        return ResponseEntity.ok(customer);
    }


}
