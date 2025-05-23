package com.pbdcompany.dto.response;

import com.pbdcompany.enums.DeliveryStatus;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsInfoResponse {
    private String logisticsCompany;
    private String trackingNumber;
    private DeliveryStatus status;
}
