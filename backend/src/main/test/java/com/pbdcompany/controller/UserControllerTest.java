package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.dto.response.RegisterResponse;
import com.pbdcompany.service.AdminService;
import com.pbdcompany.service.CustomerService;
import com.pbdcompany.service.MerchantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private CustomerService customerService;

    @Mock
    private MerchantService merchantService;

    @Mock
    private AdminService adminService;

    @Mock
    private JwtUtils jwtUtils;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void register_customer_success() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("pass");
        request.setUserType("customer");

        when(customerService.existsByUsername("newuser")).thenReturn(false);
        when(customerService.register(request)).thenReturn(new RegisterResponse());

        mockMvc.perform(post("/api/user/register")
                        .contentType("application/json")
                        .content("{\"username\":\"newuser\",\"password\":\"pass\",\"userType\":\"customer\"}"))
                .andExpect(status().isOk());
    }
}
