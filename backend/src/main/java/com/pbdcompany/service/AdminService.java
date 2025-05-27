package com.pbdcompany.service;

import com.pbdcompany.dto.response.OrderItemResponse;
import com.pbdcompany.entity.*;

import java.util.List;

public interface AdminService {
    List<Customer> getAllCustomers();
    void deleteCustomer(int id);

    List<Orders> getAllOrders();

    List<Product> getAllProducts();

    void updateProduct(Product product);

    List<Review> getAllReviews();
    void deleteReview(int id);

    List<Admin> getAllAdmins();

    List<OrderItemResponse> getAllOrderItems();
}
