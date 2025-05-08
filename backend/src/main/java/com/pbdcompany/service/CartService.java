package com.pbdcompany.service;

import com.pbdcompany.dto.request.AddToCartRequest;
import com.pbdcompany.dto.response.CartItemResponse;
import com.pbdcompany.dto.response.CartResponse;
import com.pbdcompany.entity.CartItem;
import com.pbdcompany.entity.Product;
import com.pbdcompany.repository.CartItemRepository;
import com.pbdcompany.repository.ProductRepository;
import org.glassfish.jaxb.core.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addToCart(AddToCartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = new CartItem();
        cartItem.setCustomerId("current-customer-id");
        // TODO: 将 current-customer-id替换为当前实际登陆用户的id
        cartItem.setProductId(product.getId());
        cartItem.setQuantity(request.getQuantity());

        cartItemRepository.save(cartItem);
    }
    public CartResponse getCart() {

        List<CartItem> cartItems = cartItemRepository.findByCustomerId("current-customer-id");
        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProductId())
                            .orElseThrow();
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
