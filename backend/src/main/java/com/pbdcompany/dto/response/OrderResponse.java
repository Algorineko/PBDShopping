package com.pbdcompany.dto.response;


import com.pbdcompany.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private int orderId;
    private Status status;
    private Double totalPrice;
}
