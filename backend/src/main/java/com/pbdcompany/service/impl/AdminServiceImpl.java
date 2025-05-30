package com.pbdcompany.service.impl;

import com.pbdcompany.dto.response.OrderItemResponse;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.entity.*;
import com.pbdcompany.mapper.AdminMapper;
import com.pbdcompany.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<Customer> getAllCustomers() {
        return adminMapper.findAllCustomers();
    }

    @Override
    public void deleteCustomer(int id) {
        adminMapper.deleteCustomerById(id);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return adminMapper.getAllOrders();
    }

    @Override
    public List<Product> getAllProducts() {
        return adminMapper.findAllProducts();
    }

    @Override
    public void updateProduct(Product product) {
        adminMapper.updateProduct(product);
    }

    @Override
    public List<Review> getAllReviews() {
        return adminMapper.findAllReviews();
    }

    @Override
    public void deleteReview(int id) {
        adminMapper.deleteReviewById(id);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminMapper.findAllAdmins();
    }

    @Override
    public List<OrderItemResponse> getAllOrderItems() {
        return adminMapper.findAllOrderItems();
    }

}
