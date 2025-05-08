package com.pbdcompany.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String customerId;
    private String productId;
    private Integer quantity;
    private Double totalPrice;
    private String status = "PENDING"; // PENDING,PAID,SHIPPED,DELIVERED,RETURNED

    //物流信息
    private String logisticsCompany;
    private String trackingNumber;
    private String deliveryStatus;
}
