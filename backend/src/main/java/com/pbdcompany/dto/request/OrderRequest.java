package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class OrderRequest {
    private Long productId;
    private Integer quantity;
}
