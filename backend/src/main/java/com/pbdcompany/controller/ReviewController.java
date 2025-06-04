package com.pbdcompany.controller;

import com.pbdcompany.dto.request.ReviewRequest;
import com.pbdcompany.entity.Review;
import com.pbdcompany.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<?> addReview(
            @RequestBody ReviewRequest request) {

        int customerId = request.getCustomerId();
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

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProduct(@PathVariable int productId) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Review>> getMyReviews(@PathVariable int customerId) {
        List<Review> reviews = reviewService.getReviewsByCustomerId(customerId);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable int reviewId) {
        Review review = reviewService.findById(reviewId);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        reviewService.deleteById(reviewId);
        return ResponseEntity.ok("评论删除成功");
    }



}
