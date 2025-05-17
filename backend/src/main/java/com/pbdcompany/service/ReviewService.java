package com.pbdcompany.service;

import com.pbdcompany.entity.Orders;
import com.pbdcompany.entity.Review;
import com.pbdcompany.repository.OrderRepository;
import com.pbdcompany.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    public void addReview(Long orderId, Long customerId, Integer rating, String comment, String images){
        Orders orders = orderRepository.findById(String.valueOf(orderId))
                .orElseThrow( () -> new RuntimeException("Order not found"));
        if(!"DELIVERED".equals(orders.getStatus())){
            throw new RuntimeException("Order must be delivered before review");
        }

        Review review = new Review();
        review.setOrderId(orderId);
        review.setCustomerId(customerId);
        review.setRating(rating);
        review.setComment(comment);
        review.setImages(images);

        reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
}
