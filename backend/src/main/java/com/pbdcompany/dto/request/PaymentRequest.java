package com.pbdcompany.dto.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private int orderId;
    private String paymentMethod;
    private String verificationCode;

}
