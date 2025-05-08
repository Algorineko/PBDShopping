package com.pbdcompany.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token; //JWT token
    private String userType;
    private String username;
}
