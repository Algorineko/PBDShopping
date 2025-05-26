package com.pbdcompany.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private int cartItemId;
    private int customerId;
    private int productId;
    private int quantity;
    private String selectedOptions; //用于描述物品的购买选项，比如颜色、尺寸
//    private LocalDateTime addedAt;
//    private LocalDateTime updatedAt;
}

