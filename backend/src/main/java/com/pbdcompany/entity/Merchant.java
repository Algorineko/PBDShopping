package com.pbdcompany.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {
    private int merchantId;
    private String merchantName;
    private String password; //可能有问题，右边的描述是varchar(255) = "
    private String phoneNumber;
    private String merchantAddress;
    private String headPicture;

//    private LocalDateTime addedAt;
//    private LocalDateTime updatedAt;
}
