package com.pbdcompany.mapper;

import com.pbdcompany.dto.request.OrderRequest;
import com.pbdcompany.dto.response.OrderInfoResponse;
import com.pbdcompany.dto.response.OrderResponse;
import com.pbdcompany.dto.response.SalesReportResponse;
import com.pbdcompany.entity.OrderItem;
import com.pbdcompany.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper //该应用程序在应用时，会自动为该接口创建一个代理实现对象，并且将其存入IOC容器中，成为Bean
public interface OrdersMapper {
    List<Orders> findByMerchantId(int merchantId); // 查询商家所有订单

    List<OrderInfoResponse> findCustomerOrdersByMerchantId(int merchantId);

    public List<Orders> findAll();

    public void deleteById(int id);

    public void insert(Orders Orders);

    public void update(Orders Orders);

    public Orders findById(int id);

    public OrderResponse createOrder(int customerId, OrderRequest request);


    public void insertOrderItem(OrderItem orderItem);

    List<SalesReportResponse> generateSalesReport(@Param("merchantId") Integer merchantId,
                                                  @Param("categoryId") Integer categoryId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);
}
}
