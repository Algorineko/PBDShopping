package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long orderId;
    private String paymentMethod;
    private String verificationCode;

}
