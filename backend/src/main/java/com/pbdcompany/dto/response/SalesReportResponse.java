package com.pbdcompany.dto.response;

import lombok.Data;

@Data
public class SalesReportResponse {
    private String productName;
    private String categoryName;
    private int totalQuantity;
    private double totalRevenue;
}
