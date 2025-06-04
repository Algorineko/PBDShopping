package com.pbdcompany.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnExchangeRequest {
    private int orderItemId; // 必须
    private int customerId;
    private String requestType; // "RETURN" 或 "EXCHANGE"
    private String reason;
    private String logisticsCompany; // 物流公司
    private String trackingNumber; // 运单号
}
