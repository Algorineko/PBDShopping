package com.pbdcompany.dto.response;

import lombok.Data;

@Data
public class OrderItemResponse {
    private int orderItemId;
    private int productId;
    private int quantity;
    private double price;
}
