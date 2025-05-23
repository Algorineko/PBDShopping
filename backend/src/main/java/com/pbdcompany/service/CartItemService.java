
package com.pbdcompany.service;


import com.pbdcompany.entity.CartItem;
import com.pbdcompany.mapper.CartItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartItemMapper cartItemMapper;

    // 查询所有购物车项
    public List<CartItem> findAll() {
        return cartItemMapper.findAll();
    }

    // 根据ID删除购物车项
    public void deleteById(int id) {
        cartItemMapper.deleteById(id);
    }

    // 添加购物车项
    public void insert(CartItem cartItem) {
        cartItemMapper.insert(cartItem);
    }

    // 更新购物车项信息
    public void update(CartItem cartItem) {
        cartItemMapper.update(cartItem);
    }

    // 根据ID查询购物车项信息
    public List<CartItem> findById(int id) {
        return cartItemMapper.findById(id);
    }
}