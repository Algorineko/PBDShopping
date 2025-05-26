
package com.pbdcompany.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbdcompany.dto.request.AddToCartRequest;
import com.pbdcompany.entity.CartItem;
import com.pbdcompany.service.CartItemService;
import com.pbdcompany.Utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @Mock
    private CartItemService cartItemService;

    @Mock
    private JwtUtils jwtUtils; // 新增 Mock

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }


    @Test
    public void testAddToCart_Success() throws Exception {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1);

        // 模拟 jwtUtils 返回固定 customerId
        when(jwtUtils.extractCustomerId(anyString())).thenReturn(1);

        // 模拟插入成功
        when(cartItemService.insert(any(CartItem.class))).thenReturn(1);

        mockMvc.perform(post("/api/customer/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer mockToken") // ✅ 添加 Token 头
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Item added to cart successfully"));
    }




    // ========== 测试 GET /api/customer/cart/get ==========

    @Test
    public void testGetCart_Success() throws Exception {
        List<CartItem> cartItems = Arrays.asList(
                new CartItem(1, 1, 101, 1, "选项A"),
                new CartItem(2, 1, 102, 1, "选项B")
        );

        when(cartItemService.findById(1)).thenReturn(cartItems);
        when(jwtUtils.extractCustomerId(anyString())).thenReturn(1); // 模拟返回用户ID

        mockMvc.perform(get("/api/customer/cart/get")
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productId").value(101));
    }

    // ========== 测试 DELETE /api/customer/cart/{cartItemId} ==========

    @Test
    public void testDeleteCart_Success() throws Exception {
        doNothing().when(cartItemService).deleteById(1);

        mockMvc.perform(delete("/api/customer/cart/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart deleted successfully"));
    }

    // ========== 测试 PUT /api/customer/cart/{cartItemId} ==========

    @Test
    public void testUpdateCart_Success() throws Exception {
        CartItem updatedCartItem = new CartItem(1, 1, 101, 2, "更新选项");

        doNothing().when(cartItemService).update(any(CartItem.class));

        mockMvc.perform(put("/api/customer/cart/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCartItem)))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart updated successfully"));
    }
}
