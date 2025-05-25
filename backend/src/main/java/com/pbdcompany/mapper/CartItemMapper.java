package com.pbdcompany.mapper;

import com.pbdcompany.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean
public interface CartItemMapper {

    public List<CartItem> findAll();

    public void deleteById(int id);

    public int insert(CartItem CartItem);

    public void update(CartItem CartItem);

    public List<CartItem> findByCustomerId(int customerId);
 
}
