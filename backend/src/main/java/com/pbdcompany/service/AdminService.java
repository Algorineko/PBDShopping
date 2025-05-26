package com.pbdcompany.service;

import com.pbdcompany.entity.*;
import java.util.List;

public interface AdminService {
    List<Customer> getAllCustomers();
    void deleteCustomer(int id);

    List<Orders> getAllOrders();

    List<Product> getAllProducts();
    void updateProduct(com.pbdcompany.entity.Product product);

    List<Review> getAllReviews();
    void deleteReview(int id);

    List<Admin> getAllAdmins();
    void addAdmin(Admin admin);
    void updateAdmin(Admin admin);
    void deleteAdmin(int id);
}
