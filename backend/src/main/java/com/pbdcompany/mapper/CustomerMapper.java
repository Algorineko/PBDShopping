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
    //@Select("select * from customer")
    public List<Customer> findAll();

    /**
     *  根据Id删除用户
     */

    //@Delete("delete from merchant where MerchantID = #{id}")
    public Integer deleteById(Integer id);

    /**
     *  插入一条用户数据
     */

    //@Insert("insert into customer(customerid, customername, password, money, phonenumber, address) values(#{customerId}, #{customerName}, #{password}, #{money}, #{phoneNumber}, #{address})")
    public int insert(Customer customer);

    /*
        根据Id更新用户数据
     */

    //@Update("update customer set customername = #{customerName},password = #{password}, money=#{money}, phonenumber=#{phoneNumber}, address=#{address} where customerid = #{customerId}")
    //public void update(Customer customer);
    //以下是通过不构造对象，只穿递参数的方式进行查询，此处因为使用的是官方文件，因此省略@Param参数，以简化代码
    public void update(Integer customerId, String customerName,String password,Double money,String phoneNumber,String address);

    /*
        根据用户名和密码查询用户
     */

    //一般性的查询
    //@Select("select * from customer where CustomerName = #{username} and Password = #{password}")
    public Customer findByUsernameAndPassword(
            @Param("username") String username,
            @Param("password") String password);
    //此处，不使用@Param注解也可以，但仅限于Springboot官方框架，使用阿里云的框架就会报错


}
