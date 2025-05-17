package com.pbdcompany.service;

import com.pbdcompany.dto.request.AddToCartRequest;
import com.pbdcompany.dto.response.CartItemResponse;
import com.pbdcompany.dto.response.CartResponse;
import com.pbdcompany.entity.CartItem;
import com.pbdcompany.entity.Product;
import com.pbdcompany.repository.CartItemRepository;
import com.pbdcompany.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addToCart(Long customerId, AddToCartRequest request) {
        Product product = productRepository.findById(String.valueOf(request.getProductId()))
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = new CartItem();
        cartItem.setCustomerId(customerId);
        cartItem.setProductId(request.getProductId());
        cartItem.setQuantity(request.getQuantity());

        cartItemRepository.save(cartItem);
    }
    public CartResponse getCart(Long customerId) {

        List<CartItem> cartItems = cartItemRepository.findByCustomerId(customerId);
        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(cartItem -> {
                    Product product = productRepository.findById(String.valueOf(cartItem.getProductId()))
                            .orElseThrow(() -> new RuntimeException("Product not found in cart"));
                    return new CartItemResponse(
                            cartItem.getProductId(),
                            product.getName(),
                            product.getPrice(),
                            cartItem.getQuantity()
                    );
                }).toList();
        return new CartResponse(itemResponses);
    }
}
