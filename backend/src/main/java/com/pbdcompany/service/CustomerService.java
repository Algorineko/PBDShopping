
package com.pbdcompany.service;


import com.pbdcompany.dto.request.RegisterRequest;
import com.pbdcompany.dto.response.RegisterResponse;
import com.pbdcompany.entity.Customer;
import com.pbdcompany.mapper.CustomerMapper;
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
    public int insert(Customer customer) {
       return  customerMapper.insert(customer);
    }

    // 根据ID更新用户数据
    public void update(Integer customerId, String customerName, String password, Double money, String phoneNumber, String address) {
        customerMapper.update(customerId, customerName, password, money, phoneNumber, address);
    }

    // 根据用户名和密码查询用户
    public Customer findByUsernameAndPassword(String username, String password) {
        return customerMapper.findByUsernameAndPassword(username, password);
    }

    //注册
    public RegisterResponse register(RegisterRequest request) {
        Customer customer = new Customer();
        customer.setCustomerName(request.getUsername());
        customer.setPassword(request.getPassword());
        customer.setPhoneNumber(request.getPhone());
        customer.setMoney(0.0); // 新用户初始金额为0
        customer.setAddress(""); // 默认地址为空
        customer.setHeadPicture(""); // 默认头像为空
        customerMapper.insert(customer); // 插入数据库

        //此处，对逻辑作出修改，原因：使用insert插入数据库后，可以返回Id
        //上述原因错误，插入数据库后，返回值为影响到的语句数量，而不是Id。Id会自动填充进实体类里。
        return new RegisterResponse(customer.getCustomerId(), customer.getCustomerName());
    }

    //登录
    public Customer login(String username, String password) {
        return customerMapper.findByUsernameAndPassword(username, password);
    }


}