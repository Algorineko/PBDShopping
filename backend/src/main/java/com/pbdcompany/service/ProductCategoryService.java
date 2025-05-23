
package com.pbdcompany.service;


import com.pbdcompany.entity.ProductCategory;
import com.pbdcompany.mapper.ProductCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    // 查询所有商品分类
    public List<ProductCategory> findAll() {
        return productCategoryMapper.findAll();
    }

    // 根据ID删除商品分类
    public void deleteById(int id) {
        productCategoryMapper.deleteById(id);
    }

    // 添加商品分类
    public void insert(ProductCategory productCategory) {
        productCategoryMapper.insert(productCategory);
    }

    // 更新商品分类信息
    public void update(ProductCategory productCategory) {
        productCategoryMapper.update(productCategory);
    }

    // 根据ID查询商品分类信息
    public ProductCategory findById(int id) {
        return productCategoryMapper.findById(id);
    }
}