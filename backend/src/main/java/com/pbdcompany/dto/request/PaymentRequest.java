package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private String orderId;
    private String paymentMethod;
    private String verificationCode;

}
