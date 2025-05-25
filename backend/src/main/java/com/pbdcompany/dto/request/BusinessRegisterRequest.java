package com.pbdcompany.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BusinessRegisterRequest {
    private String username;
    private String password;
    private String shopName;
    private String contactPhone;
    private String verificationCode;

    // Getters and Setters
}
