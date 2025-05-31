package com.pbdcompany.dto.request;

import com.pbdcompany.entity.LogisticsInfo;
import lombok.Data;

import java.util.List;

@Data
public class OrderShippingRequest {
    private int orderId;
    private List<LogisticsInfo> logisticsList;
}
