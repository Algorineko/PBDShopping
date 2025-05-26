package com.pbdcompany.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbdcompany.entity.*;
import com.pbdcompany.service.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private OrdersService ordersService;

    @Mock
    private ProductService productService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        List<Customer> customers = Arrays.asList(
                new Customer(0, "张三", "123456", 100.0, "123456789", "北京",""),
                new Customer(0, "李四", "123456", 200.0, "987654321", "上海","")
        );

        when(customerService.findAll()).thenReturn(customers);

        mockMvc.perform(get("/api/admin/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].customerName").value("张三"));
    }

    // 测试 /api/admin/order 接口
    @Test
    public void testGetAllOrders() throws Exception {
        List<Orders> orders = Arrays.asList(
                new Orders(0, 1,100.0, "待支付"),
                new Orders(0,2, 200.0, "已发货")
        );

        when(ordersService.findAll()).thenReturn(orders);

        mockMvc.perform(get("/api/admin/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // 测试 /api/admin/product 接口
    @Test
    public void testGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(
                new Product(0,2,1, "商品A", "描述A", 100.0),
                new Product(0,1,2, "商品B", "描述B", 200.0)
        );

        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/api/admin/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // 测试 /api/admin/review 接口
    @Test
    public void testGetAllReviews() throws Exception {
        List<Review> reviews = Arrays.asList(
                new Review(1, 1, 1, 5.0, "很好！"),
                new Review(2, 2, 1, 4.5, "不错！")
        );

        when(reviewService.findAll()).thenReturn(reviews);

        mockMvc.perform(get("/api/admin/review"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}

