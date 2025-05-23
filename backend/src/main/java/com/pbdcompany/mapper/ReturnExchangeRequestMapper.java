package com.pbdcompany.mapper;

import com.pbdcompany.dto.request.ReturnExchangeRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean
public interface ReturnExchangeRequestMapper {

    public List<ReturnExchangeRequest> findAll();

    public void deleteById(int id);

    public void insert(ReturnExchangeRequest ReturnExchangeRequest);

    public void update(ReturnExchangeRequest ReturnExchangeRequest);

    public ReturnExchangeRequest findById(int id);



}
