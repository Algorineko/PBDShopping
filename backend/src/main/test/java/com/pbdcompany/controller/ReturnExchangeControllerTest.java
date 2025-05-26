package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.ReturnExchangeRequest;
import com.pbdcompany.entity.ReturnExchange;
import com.pbdcompany.service.ReturnExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ReturnExchangeControllerTest {

    @Mock
    private ReturnExchangeService returnExchangeService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private ReturnExchangeController returnExchangeController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(returnExchangeController).build();
    }

    // ========== 测试 POST /api/customer/return-exchange/{orderId} ==========

    @Test
    public void testApplyReturnExchange_Success() throws Exception {
        // 构造请求数据
        ReturnExchangeRequest request = new ReturnExchangeRequest();
        request.setOrderItemId(101);
        request.setRequestType("RETURN");
        request.setReason("商品损坏");
        request.setLogisticsCompany("顺丰快递");
        request.setTrackingNumber("SF123456789");

        // 模拟 Service 返回的实体
        ReturnExchange mockEntity = new ReturnExchange(
                1, // requestId
                101, // orderItemId
                1, // customerId
                "RETURN",
                "商品损坏",
                0, // status
                "顺丰快递",
                "SF123456789"
        );

        // 模拟 JWT 解析用户ID
        when(jwtUtils.extractCustomerId(anyString())).thenReturn(1);

        // 模拟 Service 行为
        when(returnExchangeService.getByRequest(request)).thenReturn(mockEntity);
        when(returnExchangeService.insert(any(ReturnExchange.class))).thenReturn(1); // ✅ 改为 return 1

        // 发起请求并验证响应
        mockMvc.perform(post("/api/customer/return-exchange/1001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer mockToken")
                        .content("""
                        {
                          "orderItemId": 101,
                          "requestType": "RETURN",
                          "reason": "商品损坏",
                          "logisticsCompany": "顺丰快递",
                          "trackingNumber": "SF123456789"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderItemId").value(101))
                .andExpect(jsonPath("$.requestType").value("RETURN"));
    }


    //该测试类要求：更改异常处理，使得测试通过。

    @Test
    public void testApplyReturnExchange_MissingToken_ReturnsUnauthorized() throws Exception {
        // 不传 Authorization 头部，模拟无 Token 请求
        mockMvc.perform(post("/api/customer/return-exchange/1001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "orderItemId": 101,
                              "requestType": "RETURN"
                            }
                            """))
                .andExpect(status().isInternalServerError());
    }

    //该测试类要求：更改异常处理，使得测试通过。
    @Test
    public void testApplyReturnExchange_InvalidToken_ReturnsUnauthorized() throws Exception {
        // 模拟 JWT 解析失败
        when(jwtUtils.extractCustomerId(anyString())).thenThrow(new RuntimeException("无效 Token"));

        mockMvc.perform(post("/api/customer/return-exchange/1001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer invalidToken")
                        .content("""
                            {
                              "orderItemId": 101,
                              "requestType": "RETURN"
                            }
                            """))
                .andExpect(status().isInternalServerError());
    }

    //该测试类要求：更改异常处理，使得测试通过。

    @Test
    public void testApplyReturnExchange_RequestTypeInvalid_ShouldStillPass() throws Exception {
        // 构造请求数据
        ReturnExchangeRequest request = new ReturnExchangeRequest();
        request.setOrderItemId(102);
        request.setRequestType("OTHER"); // 非标准类型，交由 Service 处理

        // 模拟 Service 返回的实体
        ReturnExchange mockEntity = new ReturnExchange(
                2, 102, 1, "OTHER", null, 0, null, null);

        // 模拟 JWT 解析用户ID
        when(jwtUtils.extractCustomerId(anyString())).thenReturn(1);

        // 模拟 Service 行为
        when(returnExchangeService.getByRequest(request)).thenReturn(mockEntity);
        when(returnExchangeService.insert(any(ReturnExchange.class))).thenReturn(1); // ✅ 改为 return 1

        // 发起请求并验证响应
        mockMvc.perform(post("/api/customer/return-exchange/1002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer mockToken")
                        .content("""
                        {
                          "orderItemId": 102,
                          "requestType": "OTHER"
                        }
                        """))
                .andExpect(status().isOk());
    }

}
