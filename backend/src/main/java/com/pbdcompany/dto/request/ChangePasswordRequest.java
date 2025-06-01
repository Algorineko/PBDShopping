package com.pbdcompany.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ChangePasswordRequest {
     private String oldPassword;
      private String newPassword;
}
