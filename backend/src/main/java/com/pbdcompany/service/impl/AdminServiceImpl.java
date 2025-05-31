package com.pbdcompany.service.impl;

import com.pbdcompany.dto.response.OrderItemResponse;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.entity.*;
import com.pbdcompany.mapper.AdminMapper;
import com.pbdcompany.mapper.OrderItemMapper;
import com.pbdcompany.mapper.OrderMapper;
import com.pbdcompany.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;
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
        return orderMapper.selectAll().stream().map(order -> {
            OrderResponse response = new OrderResponse();
            response.setOrderId(order.getOrderId());
            response.setCustomerId(order.getCustomerId());
            response.setMerchantId(order.getMerchantId());
            response.setTotalPrice(order.getTotalPrice());
            response.setStatus(order.getStatus().name());

            // 查询订单项并转换为 OrderItemResponse
            List<OrderItem> items = orderItemMapper.findByOrderId(order.getOrderId());
            List<OrderItemResponse> itemResponses = items.stream().map(item -> {
                OrderItemResponse ir = new OrderItemResponse();
                ir.setProductId(item.getProductId());
                ir.setQuantity(item.getQuantity());
                ir.setPrice(item.getPrice());
                return ir;
            }).collect(Collectors.toList());

            response.setItems(itemResponses);
            return response;
        }).collect(Collectors.toList());
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
