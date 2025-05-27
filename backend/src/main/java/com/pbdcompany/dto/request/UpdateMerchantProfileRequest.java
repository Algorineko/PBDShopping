package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class UpdateMerchantProfileRequest {
    private String merchantName;
    private String PhoneNumber;
    private String address;
}
