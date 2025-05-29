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
    private int productId; //错误冗余
    private int quantity; //错误冗余
    private double totalPrice;
    private Status status;
    private String logisticsCompany; //错误冗余
    private String trackingNumber; //错误冗余

    //该实体类有四个属性错误冗余，即不属于orders的描述内容。但不影响与数据库的操作，暂且保留。
}
