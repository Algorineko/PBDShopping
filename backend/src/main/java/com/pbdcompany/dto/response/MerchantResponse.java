package com.pbdcompany.dto.response;

import lombok.Data;

@Data
public class MerchantResponse {
    private int merchantId;
    private String merchantName;
    private String phoneNumber;
    private String merchantAddress;
    private String headPicture;
}
