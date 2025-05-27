
package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class UpdateCustomerProfileRequest {
    private String phoneNumber;
    private String address;
}
