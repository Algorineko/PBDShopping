package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class MerchantRegisterRequest {
    private String merchantName;
    private String password;
    private String phoneNumber;
    private String merchantAddress;
    private String headPicture;
}
