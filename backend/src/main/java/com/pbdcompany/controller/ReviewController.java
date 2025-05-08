package com.pbdcompany.controller;

import com.pbdcompany.entity.CustomerUserDetails;
import com.pbdcompany.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/{orderId}")
    public ResponseEntity<?> addReview(
            @PathVariable String orderId,
            Integer rating,
            String comment,
            @RequestParam(required = false) String images,
            @AuthenticationPrincipal CustomerUserDetails userDetails) {
        String customerId = userDetails.getUsername();

        reviewService.addReview(orderId, customerId, rating, comment, images);

        return ResponseEntity.ok("Review added successfully");
    }
}
