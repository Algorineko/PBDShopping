package com.pbdcompany.mapper;

import com.pbdcompany.dto.response.ProductResponse;
import com.pbdcompany.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean
public interface ProductMapper {

    public List<Product> findAll();

    public void deleteById(int id);

    public void insert(Product Product);

    public void update(Product Product);

    public Product findById(int id);

    public List<ProductResponse> findByNameOrId(String name, int id);
 
}
