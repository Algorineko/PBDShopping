
package com.pbdcompany.service;

import org.example.springbootmybatis.entity.Cart;
import org.example.springbootmybatis.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    // 查询所有购物车
    public List<Cart> findAll() {
        return cartMapper.findAll();
    }

    // 根据ID删除购物车
    public void deleteById(int id) {
        cartMapper.deleteById(id);
    }

    // 添加购物车
    public void insert(Cart cart) {
        cartMapper.insert(cart);
    }

    // 更新购物车信息
    public void update(Cart cart) {
        cartMapper.update(cart);
    }

    // 根据ID查询购物车信息
    public Cart findById(int id) {
        return cartMapper.findById(id);
    }
}