package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class UpdateProductRequest {
    private int productId;
    private int merchantId;
    private String productName;
    private String description;
    private Double price;
    private Integer categoryId;
}

