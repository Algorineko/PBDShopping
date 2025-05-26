package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class UpdateOrderRequest {
    private int orderId;
    private int merchantId; // 权限校验用
    private String logisticsCompany;
    private String trackingNumber;
    private String newStatus; // "已发货" / "已退货" / "已换货"
}
