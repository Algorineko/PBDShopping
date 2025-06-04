package com.pbdcompany.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.OrderCreateRequest;
import com.pbdcompany.dto.request.OrderItemRequest;
import com.pbdcompany.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private JwtUtils jwtUtils;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void getOrdersByUserId_success() throws Exception {
        when(orderService.getOrdersByCustomerId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/order/customer/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createOrder_success() throws Exception {
        // 构造请求对象
        OrderCreateRequest request = new OrderCreateRequest();
        request.setCustomerId(1);
        request.setMerchantId(2);

        OrderItemRequest item = new OrderItemRequest();
        item.setProductId(1);
        item.setQuantity(1);
        request.setItems(Collections.singletonList(item));

        // 模拟服务层返回结果
        when(orderService.createOrder(any(OrderCreateRequest.class))).thenReturn(true);

        // 使用 ObjectMapper 序列化对象，避免手动拼 JSON
        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        // 发起请求并验证响应
        mockMvc.perform(post("/api/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .header("Authorization", "Bearer fake-jwt-token")) // 添加 Token
                .andExpect(status().isOk());

        // 验证 service 是否被调用一次
        verify(orderService, times(1)).createOrder(any(OrderCreateRequest.class));
    }

}
