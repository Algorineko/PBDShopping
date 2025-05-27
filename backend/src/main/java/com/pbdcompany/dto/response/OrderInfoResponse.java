package com.pbdcompany.dto.response;

import lombok.Data;

@Data
public class OrderInfoResponse {
    private int orderId;
    private int customerId;
    private int merchantId;
    private int productId;
    private int quantity;
    private String orderStatus;
    private String logisticsCompany;
    private String trackingNumber;
}
