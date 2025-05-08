package com.pbdcompany.repository;


import com.pbdcompany.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    boolean existsByUsername(String username);
}