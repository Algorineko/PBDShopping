package com.pbdcompany.dto.request;

import com.pbdcompany.enums.UserType;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String phone;
    private UserType userType;
}
