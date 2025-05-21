package com.pbdcompany.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private int customerId;
    private String customerName;
    private String password;
    private Double money;
    private String phoneNumber;
    private String address;
    private String headPicture;

//    private LocalDateTime addedAt;
//    private LocalDateTime updatedAt;
}

