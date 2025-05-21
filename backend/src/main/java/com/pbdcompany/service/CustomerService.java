
package com.pbdcompany.service;

import org.example.springbootmybatis.entity.Customer;
import org.example.springbootmybatis.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    // 查询所有用户
    public List<Customer> findAll() {
        return customerMapper.findAll();
    }

    // 根据ID删除用户
    public Integer deleteById(Integer id) {
        return customerMapper.deleteById(id);
    }

    // 添加用户
    public void insert(Customer customer) {
        customerMapper.insert(customer);
    }

    // 根据ID更新用户数据
    public void update(Integer customerId, String customerName, String password, Double money, String phoneNumber, String address) {
        customerMapper.update(customerId, customerName, password, money, phoneNumber, address);
    }

    // 根据用户名和密码查询用户
    public Customer findByUsernameAndPassword(String username, String password) {
        return customerMapper.findByUsernameAndPassword(username, password);
    }
}