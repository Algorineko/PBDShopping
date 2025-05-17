package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long productId;
    private Integer quantity;
}
