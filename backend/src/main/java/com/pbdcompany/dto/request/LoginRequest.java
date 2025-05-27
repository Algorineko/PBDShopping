package com.pbdcompany.dto.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginRequest {
    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String userType;
}
