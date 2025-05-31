package com.pbdcompany.mapper;

import com.pbdcompany.dto.response.OrderItemResponse;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {

    // ==== 用户管理 ====
    List<Customer> findAllCustomers();

    void deleteCustomerById(int id);

    // ==== 订单管理 ====

    List<OrderItemResponse> findAllOrderItems();

    // ==== 商品管理 ====
    List<Product> findAllProducts();

    void updateProduct(Product product);

    // ==== 评论管理 ====
    List<Review> findAllReviews();

    void deleteReviewById(int id);

    // ==== 管理员自身管理 ====
    List<Admin> findAllAdmins();


}
