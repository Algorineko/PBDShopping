package com.pbdcompany.controller;

import com.pbdcompany.entity.*;
import com.pbdcompany.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/customer")
    public List<Customer> getAllCustomers() {
        return adminService.getAllCustomers();
    }

    @DeleteMapping("/customer/{id}")
    public void deleteCustomer(@PathVariable int id) {
        adminService.deleteCustomer(id);
    }

    @GetMapping("/order")
    public List<Orders> getAllOrders() {
        return adminService.getAllOrders();
    }

    @PutMapping("/product")
    public void updateProduct(@RequestBody Product product) {
        adminService.updateProduct(product);
    }

    @GetMapping("/product")
    public List<Product> getAllProducts() {
        return adminService.getAllProducts();
    }

    @GetMapping("/review")
    public List<Review> getAllReviews() {
        return adminService.getAllReviews();
    }

    @DeleteMapping("/review/{id}")
    public void deleteReview(@PathVariable int id) {
        adminService.deleteReview(id);
    }

    @GetMapping("/admin")
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

}
