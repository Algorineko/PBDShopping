package com.pbdcompany.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
}
