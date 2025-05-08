package com.pbdcompany.controller;

import com.pbdcompany.dto.request.AddToCartRequest;
import com.pbdcompany.dto.response.CartResponse;
import com.pbdcompany.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("/add")
    public void addToCart(AddToCartRequest request)
    {
        cartService.addToCart(request);
    }

    @GetMapping
    public CartResponse getCart()
    {
        return cartService.getCart();
    }
}
