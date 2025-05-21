package com.pbdcompany.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    private int productImageId;
    private int productId;
    private String image;

//    private LocalDateTime addedAt;
//    private LocalDateTime updatedAt;
}
