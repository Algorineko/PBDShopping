package com.pbdcompany.dto.response;

import lombok.Data;

@Data
public class MerchantProfileResponse {
    private int merchantId;
    private String merchantName;
    private String password;
    private String phoneNumber;
    private String merchantAddress;
    private String headPicture;
}
