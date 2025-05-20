package com.pbdcompany.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private int cartId;
    private int customerId;

//    private LocalDateTime addedAt;
//    private LocalDateTime updatedAt;
}

