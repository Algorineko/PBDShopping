
package com.pbdcompany.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileResponse {
    private int customerId;
    private String customerName;
    private String phoneNumber;
    private String address;
    private double money;
}
