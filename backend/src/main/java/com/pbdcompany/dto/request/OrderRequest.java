package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class OrderRequest {
    private String productId;
    private Integer quantity;
}
