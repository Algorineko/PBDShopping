package com.pbdcompany.controller;

import com.pbdcompany.dto.request.AddToCartRequest;
import com.pbdcompany.entity.CartItem;
import com.pbdcompany.service.CartItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pbdcompany.Utils.JwtUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/cart")
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    //将 JwtUtils 改为可注入的 Bean，这样可以在测试中使用 @Mock 来模拟其行为。
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody AddToCartRequest request,
            HttpServletRequest httpServletRequest) {

        int customerId = extractCustomerIdFromToken(httpServletRequest);
        try {
            cartItemService.insert(new CartItem(
                    0,customerId,
                    request.getProductId(),
                    request.getProductId(),
                    request.getSelectedOptions()));
            return ResponseEntity.ok("Item added to cart successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to add item to cart");
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCart(HttpServletRequest httpServletRequest) {
        int customerId = extractCustomerIdFromToken(httpServletRequest);
        try {
            List<CartItem> carts = cartItemService.findById(customerId); // 假设此处筛选该用户的数据
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
            updatedCartItem.setCartItemId(cartItemId); // 确保ID一致
            cartItemService.update(updatedCartItem);
            return ResponseEntity.ok("Cart updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update cart");
        }
    }

    //5.26修改：将 JwtUtils 改为可注入的 Bean。
    // 从请求中提取用户ID
    private int extractCustomerIdFromToken(HttpServletRequest request) {
        String token = parseJwt(request);
        if (token == null) {
            throw new RuntimeException("Missing or invalid token");
        }
        return jwtUtils.extractCustomerId(token);
    }

    // 从 Header 提取 JWT Token
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // 去除 "Bearer " 前缀
        }

        return null;
    }

}
