package com.pbdcompany.controller;

import com.pbdcompany.dto.request.OrderRequest;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.service.OrdersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrdersService ordersService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    // ========== 测试 POST /api/customer/order/create ==========

    @Test
    public void testCreateOrder_Success() throws Exception {
        // 构造请求数据
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1);
        request.setTotalPrice(299.9);

        OrderRequest.OrderItemRequest item1 = new OrderRequest.OrderItemRequest();
        item1.setProductId(101);
        item1.setQuantity(2);
        item1.setPrice(149.95);
        item1.setSelectedOptions("红色,大号");

        OrderRequest.OrderItemRequest item2 = new OrderRequest.OrderItemRequest();
        item2.setProductId(102);
        item2.setQuantity(1);
        item2.setPrice(150.0);
        item2.setSelectedOptions("");

        request.setOrderItems(List.of(item1, item2));

        // 构造返回值
        OrderResponse response = new OrderResponse(1001, "已下单", 299.9);

        // 模拟服务层行为
        when(ordersService.createOrder(anyInt(), any(OrderRequest.class))).thenReturn(response);
        when(jwtUtils.extractCustomerId(anyString())).thenReturn(1); // 模拟用户ID

        // 发起请求并验证响应
        mockMvc.perform(post("/api/customer/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer mockToken")
                        .content("""
                            {
                              "customerId": 1,
                              "totalPrice": 299.9,
                              "orderItems": [
                                {
                                  "productId": 101,
                                  "quantity": 2,
                                  "price": 149.95,
                                  "selectedOptions": "红色,大号"
                                },
                                {
                                  "productId": 102,
                                  "quantity": 1,
                                  "price": 150.0,
                                  "selectedOptions": ""
                                }
                              ]
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1001))
                .andExpect(jsonPath("$.status").value("已下单"))
                .andExpect(jsonPath("$.totalPrice").value(299.9));
    }

    @Test
    public void testCreateOrder_InvalidToken_ReturnsUnauthorized() throws Exception {
        // 模拟 JWT 解析失败
        when(jwtUtils.extractCustomerId(anyString())).thenThrow(new RuntimeException("无效 Token"));

        // 发起请求并验证响应
        mockMvc.perform(post("/api/customer/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer invalidToken")
                        .content("""
                            {
                              "customerId": 1,
                              "totalPrice": 299.9,
                              "orderItems": []
                            }
                            """))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @Test
    public void testCreateOrder_NoToken_ReturnsUnauthorized() throws Exception {
        // 不传 Authorization 头部，模拟无 Token 请求
        mockMvc.perform(post("/api/customer/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "customerId": 1,
                              "totalPrice": 299.9,
                              "orderItems": []
                            }
                            """))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }
}
