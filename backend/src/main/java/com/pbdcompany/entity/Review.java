package com.pbdcompany.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private int reviewId;
    private int orderItemId;
    private int customerId;
    private double rating;
    private String comment;

//    private LocalDateTime addedAt;
//    private LocalDateTime updatedAt;
}

