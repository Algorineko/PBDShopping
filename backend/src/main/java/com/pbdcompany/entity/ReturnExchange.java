package com.pbdcompany.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnExchange {
    private int requestId; // 主键
    private int orderItemId; // 外键
    private int customerId; // 关联客户ID
    private String requestType; // "RETURN" 或 "EXCHANGE"
    private String reason;
    private int status; // 默认值如 0 表示待审核
    private String logisticsCompany; // 物流公司
    private String trackingNumber; // 运单号

//    private LocalDateTime addedAt;
//    private LocalDateTime updatedAt;
}