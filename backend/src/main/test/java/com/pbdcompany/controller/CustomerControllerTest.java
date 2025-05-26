package com.pbdcompany.controller;

import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.dto.response.RegisterResponse;
import com.pbdcompany.entity.Customer;
import com.pbdcompany.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    // ========== 测试 POST /api/customer/register ==========

    @Test
    public void testRegister_Success() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setPhone("12345678901");


        RegisterResponse response = new RegisterResponse();
        //response.setMessage("注册成功");
        response.setCustomerId(1);
        response.setUsername("testuser");

        when(customerService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/customer/register")
                        .param("username", "testuser")
                        .param("password", "password123")
                        .param("phone", "12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    // ========== 测试 GET /api/customer/login ==========

    @Test
    public void testLogin_Success() throws Exception {
        Customer mockCustomer = new Customer(1, "testuser", "password123", 100.0, "12345678901", "北京市", "");

        when(customerService.login("testuser", "password123")).thenReturn(mockCustomer);

        mockMvc.perform(get("/api/customer/login")
                        .param("username", "testuser")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("testuser"))
                .andExpect(jsonPath("$.money").value(100.0));
    }

    @Test
    public void testLogin_Failure() throws Exception {
        when(customerService.login("wronguser", "wrongpass")).thenReturn(null);

        mockMvc.perform(get("/api/customer/login")
                        .param("username", "wronguser")
                        .param("password", "wrongpass"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("\"用户名或密码错误\"")); // 修正匹配值
    }

}
