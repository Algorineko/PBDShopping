package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String userType; // "customer" "merchant" or "admin"
}
