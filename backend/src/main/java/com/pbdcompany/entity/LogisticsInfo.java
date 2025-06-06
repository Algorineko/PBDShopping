package com.pbdcompany.entity;

import com.pbdcompany.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogisticsInfo {
    private int orderItemId;
    private String logisticsCompany;
    private String trackingNumber;
    private Status status;
}

