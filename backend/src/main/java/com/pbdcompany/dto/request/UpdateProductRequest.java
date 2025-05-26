package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class UpdateProductRequest {
    private int productId;
    private int merchantId; // 权限校验用
    private String productName;
    private String description;
    private Double price;
}
