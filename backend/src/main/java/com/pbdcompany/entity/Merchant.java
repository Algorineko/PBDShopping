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
    private String password;
    private String phoneNumber;
    private String merchantAddress;
    private String headPicture;

}
