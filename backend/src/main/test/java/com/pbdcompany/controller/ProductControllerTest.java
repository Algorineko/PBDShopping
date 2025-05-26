package com.pbdcompany.controller;

import com.pbdcompany.dto.response.ProductResponse;
import com.pbdcompany.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    // ========== 测试 GET /api/customer/product/search ==========

    @Test
    public void testSearchProduct_ByName() throws Exception {
        // 准备模拟数据
        List<ProductResponse> mockProducts = Arrays.asList(
                new ProductResponse(1, "商品A", 100.0),
                new ProductResponse(2, "商品B", 150.0)
        );

        // 模拟服务层返回值
        when(productService.findByNameOrId("商品A", 0)).thenReturn(mockProducts);

        // 发起请求并验证响应
        mockMvc.perform(get("/api/customer/product/search")
                        .param("name", "商品A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("商品A"))
                .andExpect(jsonPath("$[0].price").value(100.0));
    }

    @Test
    public void testSearchProduct_ById() throws Exception {
        // 准备模拟数据
        List<ProductResponse> mockProducts = Arrays.asList(
                new ProductResponse(1, "商品A", 100.0)
        );

        // 模拟服务层返回值
        when(productService.findByNameOrId(null, 1)).thenReturn(mockProducts);

        // 发起请求并验证响应
        mockMvc.perform(get("/api/customer/product/search")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].name").value("商品A"));
    }

    @Test
    public void testSearchProduct_NoParams_ReturnsEmptyList() throws Exception {
        // 模拟服务层返回空列表
        when(productService.findByNameOrId(null, 0)).thenReturn(List.of());

        // 发起请求并验证响应
        mockMvc.perform(get("/api/customer/product/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
