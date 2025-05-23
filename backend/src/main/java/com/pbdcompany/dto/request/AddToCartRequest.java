package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class AddToCartRequest {
    private int productId;
    private Integer quantity;
    private String selectedOptions;
}
