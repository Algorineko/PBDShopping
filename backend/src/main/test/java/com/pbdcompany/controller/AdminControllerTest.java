package com.pbdcompany.controller;

import com.pbdcompany.entity.Customer;
import com.pbdcompany.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void getAllCustomers() throws Exception {
        when(adminService.getAllCustomers()).thenReturn(Arrays.asList(new Customer()));
        mockMvc.perform(get("/api/admin/customer"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/admin/customer/1"))
                .andExpect(status().isOk());

        verify(adminService, times(1)).deleteCustomer(1);
    }

    @Test
    void getAllOrders() throws Exception {
        mockMvc.perform(get("/api/admin/order"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllProducts() throws Exception {
        mockMvc.perform(get("/api/admin/product"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllReviews() throws Exception {
        mockMvc.perform(get("/api/admin/review"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllAdmins() throws Exception {
        mockMvc.perform(get("/api/admin/admin"))
                .andExpect(status().isOk());
    }
}
