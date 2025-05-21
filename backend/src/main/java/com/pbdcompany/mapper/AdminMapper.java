package com.pbdcompany.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.springbootmybatis.entity.Admin;

import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean
public interface AdminMapper {
    
    public List<Admin> findAll();
    
    public void deleteById(int id);
    
    public void insert(Admin Admin);

    public void update(Admin Admin);

    public Admin findById(int id);
 
}
