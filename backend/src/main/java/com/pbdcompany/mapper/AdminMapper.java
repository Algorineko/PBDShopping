package com.pbdcompany.mapper;

import com.pbdcompany.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {

    // ==== 用户管理 ====
    List<Customer> findAllCustomers();

    Customer findCustomerById(int id);

    void deleteCustomerById(int id);

    // ==== 订单管理 ====
    List<Orders> findAllOrders();

    Orders findOrderById(int id);

    // ==== 商品管理 ====
    List<Product> findAllProducts();

    Product findProductById(int id);

    void updateProduct(Product product);

    // ==== 评论管理 ====
    List<Review> findAllReviews();

    Review findReviewById(int id);

    void deleteReviewById(int id);

    // ==== 管理员自身管理 ====
    List<Admin> findAllAdmins();

    Admin findAdminById(int id);

    void insertAdmin(Admin admin);

    void updateAdmin(Admin admin);

    void deleteAdminById(int id);
}
