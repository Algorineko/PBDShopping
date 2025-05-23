package com.pbdcompany.dto.response;

import com.pbdcompany.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemTrackingResponse {
    private int orderItemId;
    private int productId;
    private int quantity;
    private String logisticsCompany;
    private String trackingNumber;
    private DeliveryStatus status;
}
