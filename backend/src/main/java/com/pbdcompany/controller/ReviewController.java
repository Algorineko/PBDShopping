package com.pbdcompany.controller;

import com.pbdcompany.Utils.JwtUtils;
import com.pbdcompany.dto.request.ReviewRequest;
import com.pbdcompany.entity.Review;
import com.pbdcompany.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtUtils jwtUtils;

    // 提取 Token 方法
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    // 从 Token 提取 Customer ID
    private int extractCustomerId(HttpServletRequest request) {
        String token = parseJwt(request);
        if (token == null) {
            throw new RuntimeException("Missing or invalid token");
        }
        return jwtUtils.extractCustomerId(token);
    }
    @PostMapping("/{orderId}")
    public ResponseEntity<?> addReview(
            @PathVariable int orderId,
            @RequestBody ReviewRequest request,
            HttpServletRequest httpServletRequest) {

        int customerId = extractCustomerId(httpServletRequest);

        // 校验必要字段
        if (request.getOrderItemId() == null || request.getRating() == null || request.getComment() == null) {
            return ResponseEntity.badRequest().body("缺少必要参数");
        }

        // 构造 Review 对象
        Review review = new Review();
        review.setOrderItemId(request.getOrderItemId());
        review.setCustomerId(customerId);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        // 添加评论
        reviewService.insert(review);

        return ResponseEntity.ok("评论提交成功");
    }

}
