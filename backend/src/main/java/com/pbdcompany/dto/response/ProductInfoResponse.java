package com.pbdcompany.dto.response;

import lombok.Data;

@Data
public class ProductInfoResponse {
    private int productId;
    private int categoryId;
    private int merchantId;
    private String productName;
    private String description;
    private Double price;
}
