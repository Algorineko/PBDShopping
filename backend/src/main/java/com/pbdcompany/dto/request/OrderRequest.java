package com.pbdcompany.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private int CustomerId;
    private int merchantId;
    private double totalPrice;
    private List<OrderItemRequest> items;
}
