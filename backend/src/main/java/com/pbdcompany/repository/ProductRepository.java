package com.pbdcompany.repository;

import com.pbdcompany.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByNameContainingOrId(String name,  String id);
}
