package com.pbdcompany.repository;


import com.pbdcompany.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    boolean existsByUsername(String username);

    Optional<Customer> findByUsername(String username);
}