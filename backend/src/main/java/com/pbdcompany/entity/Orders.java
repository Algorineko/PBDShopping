package com.pbdcompany.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private Long productId;
    private Integer quantity;
    private Double totalPrice;
    private String status = "PENDING"; // PENDING,PAID,SHIPPED,DELIVERED,RETURNED

    private Boolean isReturn = false; //是否退货
    private Boolean isExchange = false; //是否换货
    private String returnReason;
    private String exchangeReason;
    //物流信息
    private String logisticsCompany;
    private String trackingNumber;
    private String deliveryStatus;
}
