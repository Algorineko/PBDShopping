package com.pbdcompany.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Business {
    private Long id;
    private String username;
    private String password;
    private String shopName;
    private String contactPhone;
    private String verificationCode;
   // private LocalDateTime createdAt;
}
