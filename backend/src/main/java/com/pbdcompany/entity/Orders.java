package com.pbdcompany.entity;

import com.pbdcompany.enums.Status;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    private int orderId;
    private int customerId;
    private int merchantId;
    private int productId;
    private int quantity;
    private double totalPrice;
    private Status status;
    private String logisticsCompany;
    private String trackingNumber;
}
