package com.pbdcompany.mapper;

import com.pbdcompany.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean
public interface ProductCategoryMapper {

    ProductCategory findById(int categoryId);
}
