package com.pbdcompany.dto.request;

import com.pbdcompany.enums.UserType;
import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private UserType userType; // "customer" "merchant" or "admin"
}
