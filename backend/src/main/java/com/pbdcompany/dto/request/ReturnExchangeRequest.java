package com.pbdcompany.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReturnExchangeRequest {
    private int orderItemId; // 必须
    private String requestType; // "RETURN" 或 "EXCHANGE"
    private String reason;
    private String logisticsCompany; // 物流公司
    private String trackingNumber; // 运单号
}
