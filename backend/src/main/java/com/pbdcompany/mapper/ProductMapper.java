package com.pbdcompany.mapper;

import com.pbdcompany.dto.request.AddProductRequest;
import com.pbdcompany.dto.response.ProductResponse;
import com.pbdcompany.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean

public interface ProductMapper {

    List<Product> findAll();
    void deleteById(int id);
    void insert(Product product);
    void update(Product product);
    Product findById(int id);

    List<ProductResponse> findByName(String name);
    List<Product> findByMerchantId(int merchantId); // 查询商家自己的商品
    void updateSelective(Product product); // 可选字段更新

    int insertProduct(AddProductRequest request);

    int deleteProductById(int productId);

}

