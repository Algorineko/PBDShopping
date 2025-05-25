package com.pbdcompany.mapper;

import com.pbdcompany.dto.request.ReturnExchangeRequest;
import com.pbdcompany.entity.ReturnExchange;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean
public interface ReturnExchangeMapper {

    public List<ReturnExchange> findAll();

    public void deleteById(int id);

    public int insert(ReturnExchange ReturnExchange);

    public void update(ReturnExchange ReturnExchange);

    public ReturnExchange findById(int id);


}
