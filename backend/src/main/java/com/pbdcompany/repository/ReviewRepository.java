package com.pbdcompany.repository;

import com.pbdcompany.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review ,String> {
    List<Review> findByOrderId(Long orderId);
}
