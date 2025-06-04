package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.AddToCartRequest;
import com.pbdcompany.entity.CartItem;
import com.pbdcompany.service.CartItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/cart")
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody AddToCartRequest request) {

        cartItemService.insert(new CartItem(
                0,
                request.getCustomerId(),
                request.getProductId(),
                request.getQuantity(),
                request.getSelectedOptions()
        ));
        return ResponseEntity.ok("Item added to cart successfully");
    }

    public String parseJwt(HttpServletRequest request) {
        return JwtUtils.parseJwt(request);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCart(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }

        String token = authHeader.substring(7);
        String username = jwtUtils.getUsernameFromToken(token);
        int customerId = jwtUtils.extractCustomerId(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无效的 Token");
        }

        try {
            List<CartItem> carts = cartItemService.findByCustomerId(customerId); // 筛选该用户的数据
            return ResponseEntity.ok(carts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to retrieve cart data");
        }
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> deleteCart(@PathVariable int cartItemId) {
        try {
            cartItemService.deleteById(cartItemId);
            return ResponseEntity.ok("Cart deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete cart");
        }
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<?> updateCart(@PathVariable int cartItemId, @RequestBody CartItem updatedCartItem) {
        try {
            updatedCartItem.setCartItemId(cartItemId);
            cartItemService.update(updatedCartItem);
            return ResponseEntity.ok("Cart updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update cart");
        }
    }


}
