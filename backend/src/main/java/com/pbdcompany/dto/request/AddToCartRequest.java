package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class AddToCartRequest {
    private int customerId;
    private int productId;
    private int quantity;
    private String selectedOptions;
}
