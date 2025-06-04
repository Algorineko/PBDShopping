package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.UpdateCustomerProfileRequest;
import com.pbdcompany.dto.response.CustomerProfileResponse;
import com.pbdcompany.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @Mock
    private JwtUtils jwtUtils;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        when(jwtUtils.getUsernameFromToken("validtoken")).thenReturn("testuser");
    }

    @Test
    void getCurrentUserProfile_success() throws Exception {
        CustomerProfileResponse response = new CustomerProfileResponse();
        when(customerService.getCustomerProfileByUsername("testuser")).thenReturn(response);

        mockMvc.perform(get("/api/customer/profile")
                        .header("Authorization", "Bearer validtoken"))
                .andExpect(status().isOk());
    }

    @Test
    void updateCurrentUserProfile_success() throws Exception {
        UpdateCustomerProfileRequest request = new UpdateCustomerProfileRequest();
        request.setPhoneNumber("12345678901");

        when(customerService.updateCustomerProfile("testuser", request)).thenReturn(true);

        mockMvc.perform(put("/api/customer/putProfile")
                        .header("Authorization", "Bearer validtoken")
                        .contentType("application/json")
                        .content("{\"phoneNumber\":\"12345678901\"}"))
                .andExpect(status().isOk());
    }
}
