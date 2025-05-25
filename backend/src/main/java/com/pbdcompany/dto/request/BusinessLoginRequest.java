package com.pbdcompany.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessLoginRequest {
    private String username;
    private String password;

    // Getters and Setters
}
