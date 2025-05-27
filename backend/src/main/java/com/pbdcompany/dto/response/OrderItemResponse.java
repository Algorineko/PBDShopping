package com.pbdcompany.dto.response;

import lombok.Data;

@Data
public class OrderItemResponse {
    private int orderItemId;
    private int productId;
    private String productName;
    private Integer quantity;
    private Double price;
    private String status;
    private String customerName;
}
