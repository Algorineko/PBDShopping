package com.pbdcompany.controller;

import com.pbdcompany.dto.request.AddToCartRequest;
import com.pbdcompany.dto.response.CartResponse;
import com.pbdcompany.entity.CartItem;
import com.pbdcompany.entity.CustomerUserDetails;
import com.pbdcompany.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody AddToCartRequest request,
            @AuthenticationPrincipal CustomerUserDetails userDetails){
        String customerId = userDetails.getCustomerId();
        cartService.addToCart(customerId,request);

        return ResponseEntity.ok("Item added to cart successfully");
    }

    @GetMapping
    public ResponseEntity<?> getCart(
            @AuthenticationPrincipal CustomerUserDetails userDetails) {
        String customerId = userDetails.getCustomerId();
        CartResponse cartResponse = cartService.getCart(customerId);
        return ResponseEntity.ok(cartResponse);

    }
}
