package com.pbdcompany.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory {
    private int categoryId;
    private String categoryName;
    private String description;
    private String image;

//    private LocalDateTime addedAt;
//    private LocalDateTime updatedAt;
}
