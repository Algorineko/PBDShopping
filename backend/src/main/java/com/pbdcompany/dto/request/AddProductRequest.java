package com.pbdcompany.dto.request;


import lombok.Data;

@Data
public class AddProductRequest {
    private String productName;
    private Double price;
    private String description;
    private Integer stock;
    private String category;
    private Integer merchantId;
}
