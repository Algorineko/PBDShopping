package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.AddProductRequest;
import com.pbdcompany.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private JwtUtils jwtUtils;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void getProductsByMerchant_success() throws Exception {
        when(productService.getProductsByMerchant(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/merchant/product/list").param("merchantId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void addProduct_success() throws Exception {
        AddProductRequest request = new AddProductRequest();
        request.setMerchantId(1);
        request.setCategoryId(2);
        request.setProductName("product");
        request.setPrice(100);
        request.setDescription("desc");

        mockMvc.perform(post("/api/merchant/product/add")
                        .contentType("application/json")
                        .content("{\"merchantId\":1,\"categoryId\":2,\"productName\":\"product\",\"price\":100,\"description\":\"desc\"}"))
                .andExpect(status().isOk());
    }
}
