package com.pbdcompany.controller;

import com.pbdcompany.entity.Customer;
import com.pbdcompany.entity.Orders;
import com.pbdcompany.entity.Product;
import com.pbdcompany.entity.Review;
import com.pbdcompany.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private CustomerService customerService;

    /**
     *  获取所有用户信息
     */
    @GetMapping("/customer")
    public List<Customer> getAllCustomers(){
        return customerService.findAll();
    }

    @Autowired
    private OrdersService ordersService;

    /**
     *  获取所有订单信息
     */
    @GetMapping("/order")
    public List<Orders> getAllOrders(){
        return ordersService.findAll();
    }
    @Autowired
    private ProductService productService;

    /**
     *   获取所有商品信息
     */
    @GetMapping("/product")
    public List<Product> getAllProducts(){

        return productService.findAll();
    }

    @Autowired
    private ReviewService  reviewService;
    /**
     * 获取所有评论
     */
    @GetMapping("/review")
    public List<Review> getAllReviews(){
        return reviewService.findAll();
    }

}
