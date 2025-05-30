package com.pbdcompany.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {
    private int orderId;
    private int CustomerId;
    private int merchantId;
    private double totalPrice;
    private String status;
    private List<OrderItemResponse> items;
}
