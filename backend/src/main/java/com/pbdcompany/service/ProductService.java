
package com.pbdcompany.service;


import com.pbdcompany.dto.response.ProductResponse;
import com.pbdcompany.entity.Product;
import com.pbdcompany.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    // 查询所有商品
    public List<Product> findAll() {
        return productMapper.findAll();
    }

    // 根据ID删除商品
    public void deleteById(int id) {
        productMapper.deleteById(id);
    }

    // 添加商品
    public void insert(Product product) {
        productMapper.insert(product);
    }

    // 更新商品信息
    public void update(Product product) {
        productMapper.update(product);
    }

    // 根据ID查询商品信息
    public Product findById(int id) {
        return productMapper.findById(id);
    }

    // 根据Id或名称查询商品信息
    public List<ProductResponse> findByNameOrId(String name, int id) {
        return productMapper.findByNameOrId(name, id);
    }
}