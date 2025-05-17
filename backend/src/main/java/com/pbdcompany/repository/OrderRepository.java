package com.pbdcompany.repository;

import com.pbdcompany.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, String> {
    Optional<Orders> findByIdAndStatus(Long orderId, String status);

    Optional<Orders> findById(Long orderId);
}
