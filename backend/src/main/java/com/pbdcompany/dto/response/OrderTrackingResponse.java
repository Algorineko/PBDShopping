package com.pbdcompany.dto.response;

import lombok.Data;

import java.util.Map;

@Data
public class OrderTrackingResponse {
    private int orderId;
    private Map<Integer, LogisticsInfoResponse> itemLogistics; // key: orderItemId
}
