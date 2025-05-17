package com.pbdcompany.repository;

import com.pbdcompany.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
    List<CartItem> findByCustomerId(Long customerId);
}
