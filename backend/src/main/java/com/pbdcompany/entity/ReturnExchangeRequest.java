package com.pbdcompany.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnExchangeRequest {
    private int requestId;
    private int orderItemId;
    private int customerId;
    private String requestType;
    private String reason;
    private int status;
    
//    private LocalDateTime addedAt;
//    private LocalDateTime updatedAt;
}

