package com.pbdcompany.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    private int orderId;
    private int customerId;
    private double totalPrice;
    private String status;

//    private LocalDateTime addedAt;
//    private LocalDateTime updatedAt;
}
