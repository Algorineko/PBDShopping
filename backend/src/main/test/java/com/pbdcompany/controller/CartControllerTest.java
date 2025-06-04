package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.AddToCartRequest;
import com.pbdcompany.entity.CartItem;
import com.pbdcompany.service.CartItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartItemService cartItemService;

    private MockMvc mockMvc;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        // 模拟返回值
        when(jwtUtils.getUsernameFromToken("validtoken")).thenReturn("testuser");
        when(jwtUtils.extractCustomerId("validtoken")).thenReturn(1);
    }

    @Test
    void addToCart() throws Exception {
        AddToCartRequest request = new AddToCartRequest();
        request.setCustomerId(1);
        request.setProductId(100);
        request.setQuantity(2);

        mockMvc.perform(post("/api/customer/cart/add")
                        .contentType("application/json")
                        .content("{\"customerId\":1,\"productId\":100,\"quantity\":2}"))
                .andExpect(status().isOk());

        verify(cartItemService, times(1)).insert(any(CartItem.class));
    }

    @Test
    void getCart() throws Exception {
        mockMvc.perform(get("/api/customer/cart/get")
                        .header("Authorization", "Bearer validtoken"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCart() throws Exception {
        mockMvc.perform(delete("/api/customer/cart/123"))
                .andExpect(status().isOk());

        verify(cartItemService, times(1)).deleteById(123);
    }

    @Test
    void updateCart() throws Exception {
        CartItem updated = new CartItem(123, 1, 100, 3, null);
        mockMvc.perform(put("/api/customer/cart/123")
                        .contentType("application/json")
                        .content("{\"cartItemId\":123,\"customerId\":1,\"productId\":100,\"quantity\":3}"))
                .andExpect(status().isOk());

        verify(cartItemService, times(1)).update(any(CartItem.class));
    }
}
