package com.pbdcompany.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnExchangeRequest {
    private int requestId;
    private int orderItemId; // 必须
    private int customerId;
    private String requestType; // "RETURN" 或 "EXCHANGE"
    private String reason;
    private int status; // 可选，默认值如 0 表示待审核
    private String logisticsCompany; // 物流公司
    private String trackingNumber; // 运单号
}


