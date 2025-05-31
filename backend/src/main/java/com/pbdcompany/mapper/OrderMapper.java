package com.pbdcompany.mapper;

import com.pbdcompany.dto.request.OrderShippingRequest;
import com.pbdcompany.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
     List<Orders> selectAll();

    void insert(Orders order);

    List<Orders> findByCustomerId(int CustomerId);

    List<Orders> findByMerchantId(int merchantId);

    boolean shipOrder(OrderShippingRequest request);

    void updateOrderStatusToShipped(int orderId);
}
