package com.pbdcompany.mapper;

import com.pbdcompany.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean
public interface CustomerMapper {

    /**
     *  查询所有用户
     */
    List<Customer> findAll();

    /**
     *  根据Id删除用户
     */

     Integer deleteById(Integer id);

    /**
     *  插入一条用户数据
     */
    void insert(Customer customer);

    /*
        根据Id更新用户数据
     */
    void update(Integer customerId, String customerName, String password, Double money, String phoneNumber, String address);

    /*
        根据用户名和密码查询用户
     */

   Customer findByUsernameAndPassword(
           @Param("username") String username,
           @Param("password") String password);

    Customer findByUsername(String username);
}
