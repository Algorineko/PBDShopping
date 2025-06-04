package com.pbdcompany.mapper;

import com.pbdcompany.dto.response.ProductInfoResponse;
import com.pbdcompany.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean

public interface ProductMapper {

    List<Product> findAll();

    Product findById(int productId);

    List<Product> findByMerchantId(int merchantId);

    List<Product> findByCategoryId(int categoryId);

    List<Product> findByNameLike(@Param("name") String name);

    int insert(Product product);

    // 修改商品信息
    int update(Product product);

    // 删除商品
    int deleteById(int productId);

    ProductInfoResponse getProductById(int productId);


}

