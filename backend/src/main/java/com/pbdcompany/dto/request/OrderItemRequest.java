package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private int productId;
    private int quantity;
}
