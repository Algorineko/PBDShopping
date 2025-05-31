package com.pbdcompany.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequest {
    private int customerId;
    private int merchantId;
    private List<OrderItemRequest> items;
}
