package com.pbdcompany;

import com.pbdcompany.dto.response.CartItemResponse;
import com.pbdcompany.dto.response.CartResponse;
import com.pbdcompany.entity.CartItem;
import com.pbdcompany.entity.Product;
import com.pbdcompany.repository.CartItemRepository;
import com.pbdcompany.repository.ProductRepository;
import com.pbdcompany.service.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CartServiceTest {
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void testGetCart() {
        // Arrange
        String customerId = "customer123";
        Product product1 = new Product("p1", "Product 1", 10.0);
        Product product2 = new Product("p2", "Product 2", 20.0);

        CartItem cartItem1 = new CartItem("ci1", customerId, "p1", 2);
        CartItem cartItem2 = new CartItem("ci2", customerId, "p2", 1);

        when(cartItemRepository.findByCustomerId(customerId))
                .thenReturn(Arrays.asList(cartItem1, cartItem2));

        when(productRepository.findById("p1")).thenReturn(Optional.of(product1));
        when(productRepository.findById("p2")).thenReturn(Optional.of(product2));

        // Act
        CartResponse cartResponse = cartService.getCart(customerId);

        // Assert
        assertNotNull(cartResponse);
        assertEquals(2, cartResponse.getItems().size());

        CartItemResponse item1 = cartResponse.getItems().get(0);
        assertEquals("p1", item1.getProductId());
        assertEquals("Product 1", item1.getProductName());
        assertEquals(10.0, item1.getPrice());
        assertEquals(2, item1.getQuantity());

        CartItemResponse item2 = cartResponse.getItems().get(1);
        assertEquals("p2", item2.getProductId());
        assertEquals("Product 2", item2.getProductName());
        assertEquals(20.0, item2.getPrice());
        assertEquals(1, item2.getQuantity());

        verify(cartItemRepository, times(1)).findByCustomerId(customerId);
        verify(productRepository, times(1)).findById("p1");
        verify(productRepository, times(1)).findById("p2");
    }

    @Test
    void testGetCart_ProductNotFound() {
        // Arrange
        String customerId = "customer123";
        CartItem cartItem = new CartItem("ci1", customerId, "p1", 2);

        when(cartItemRepository.findByCustomerId(customerId))
                .thenReturn(Arrays.asList(cartItem));

        when(productRepository.findById("p1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            cartService.getCart(customerId);
        });

        verify(cartItemRepository, times(1)).findByCustomerId(customerId);
        verify(productRepository, times(1)).findById("p1");
    }
}
