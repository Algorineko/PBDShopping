package com.pbdcompany.controller;

import com.pbdcompany.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/{orderId}")
    public ResponseEntity<?> addReview(
            @PathVariable Long orderId,
            Integer rating,
            String comment,
            @RequestParam(required = false) String images) {
        Long customerId = 10001L;
        //TODO 使用TOKEN来获取ID

        reviewService.addReview(orderId, customerId, rating, comment, images);

        return ResponseEntity.ok("Review added successfully");
    }
}
