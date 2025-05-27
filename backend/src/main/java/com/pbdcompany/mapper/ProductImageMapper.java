package com.pbdcompany.mapper;

import com.pbdcompany.entity.Product;
import com.pbdcompany.entity.ProductImage;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean
public interface ProductImageMapper {

    public List<ProductImage> findAll();

    public void deleteById(int id);

    public int insert(ProductImage ProductImage);

    public void update(ProductImage ProductImage);

    public ProductImage findById(int id);

    public ProductImage findByProductId(int productId);
}
