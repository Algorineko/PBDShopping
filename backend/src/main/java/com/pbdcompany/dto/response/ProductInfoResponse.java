package com.pbdcompany.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ProductInfoResponse {
    private int productId;
    private int categoryId;
    private String categoryName;
    private int merchantId;
    private String productName;
    private String description;
    private Double price;
    private List<String> images;
}
