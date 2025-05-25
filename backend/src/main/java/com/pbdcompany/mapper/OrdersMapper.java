package com.pbdcompany.mapper;

import com.pbdcompany.dto.request.OrderRequest;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.entity.OrderItem;
import com.pbdcompany.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean
public interface OrdersMapper {

    public List<Orders> findAll();

    public void deleteById(int id);

    public int insert(Orders Orders);

    public void update(Orders Orders);

    public Orders findById(int id);


}
