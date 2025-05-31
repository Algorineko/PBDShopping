package com.pbdcompany.dto.request;


import lombok.Data;

import java.util.List;

@Data
public class AddProductRequest {
    private String productName;
    private double price;
    private String description;
    private int categoryId;
    private int merchantId;

    private List<String> images;
}
