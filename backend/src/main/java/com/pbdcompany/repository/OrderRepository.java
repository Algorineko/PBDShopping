package com.pbdcompany.repository;

import com.pbdcompany.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByIdAndStatus(String orderId,String status);
}
