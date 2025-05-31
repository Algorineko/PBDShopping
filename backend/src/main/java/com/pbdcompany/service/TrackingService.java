package com.pbdcompany.service;

import com.pbdcompany.dto.response.LogisticsInfoResponse;
import com.pbdcompany.dto.response.OrderTrackingResponse;
import com.pbdcompany.entity.LogisticsInfo;
import com.pbdcompany.entity.OrderItem;
import com.pbdcompany.mapper.LogisticsInfoMapper;
import com.pbdcompany.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrackingService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private LogisticsInfoMapper logisticsInfoMapper;

    public OrderTrackingResponse getTrackingInfoByOrderId(int orderId) {
        List<OrderItem> items = orderItemMapper.findByOrderId(orderId);
        Map<Integer, LogisticsInfoResponse> logisticsMap = new HashMap<>();

        for (OrderItem item : items) {
            LogisticsInfo logistics = logisticsInfoMapper.findByOrderItemId(item.getOrderItemId());
            if (logistics != null) {
                LogisticsInfoResponse response = new LogisticsInfoResponse();
                response.setLogisticsCompany(logistics.getLogisticsCompany());
                response.setTrackingNumber(logistics.getTrackingNumber());
                response.setStatus(logistics.getStatus());

                logisticsMap.put(item.getOrderItemId(), response);

            }
        }

        OrderTrackingResponse result = new OrderTrackingResponse();
        result.setOrderId(orderId);
        result.setItemLogistics(logisticsMap);

        return result;
    }
}

