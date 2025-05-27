package com.pbdcompany.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GenerateReportRequest {
    private Integer merchantId;
    private Integer categoryId;
    private LocalDate startDate;
    private LocalDate endDate;
}
