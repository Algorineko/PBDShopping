package com.pbdcompany.mapper;

import com.pbdcompany.entity.Review;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean
public interface ReviewMapper {

    public List<Review> findAll();

    public void deleteById(int id);

    public void insert(Review Review);

    public void update(Review Review);

    public Review findById(int id);
 
}
