package com.pbdcompany.dto.response;

import lombok.Data;

@Data
public class MerchantProfileResponse {
    private Long merchantId;
    private String shopName;
    private String ownerName;
    private String contactPhone;
    private String address;
    private String description;
    private String registrationTime;
}
