package com.pbdcompany.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingResponse {
    private String logisticsCompany; //物流公司
    private String trackingNumber; //物流单号
    private DeliveryStatus status;
}

