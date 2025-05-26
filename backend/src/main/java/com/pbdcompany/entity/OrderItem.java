package com.pbdcompany.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int productId;
    private int quantity;
    private double price;

}

