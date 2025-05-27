package com.pbdcompany.dto.response;

import com.pbdcompany.enums.Status;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsInfoResponse {
    private String logisticsCompany;
    private String trackingNumber;
    private Status status;
}
