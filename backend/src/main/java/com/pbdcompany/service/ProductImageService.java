
package com.pbdcompany.service;

import org.example.springbootmybatis.entity.ProductImage;
import org.example.springbootmybatis.mapper.ProductImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {

    @Autowired
    private ProductImageMapper productImageMapper;

    // 查询所有商品图片
    public List<ProductImage> findAll() {
        return productImageMapper.findAll();
    }

    // 根据ID删除商品图片
    public void deleteById(int id) {
        productImageMapper.deleteById(id);
    }

    // 添加商品图片
    public void insert(ProductImage productImage) {
        productImageMapper.insert(productImage);
    }

    // 更新商品图片信息
    public void update(ProductImage productImage) {
        productImageMapper.update(productImage);
    }

    // 根据ID查询商品图片信息
    public ProductImage findById(int id) {
        return productImageMapper.findById(id);
    }
}