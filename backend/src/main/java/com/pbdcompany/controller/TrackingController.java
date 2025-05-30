package com.pbdcompany.controller;

import com.pbdcompany.dto.response.OrderTrackingResponse;
import com.pbdcompany.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order/tracking")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    // 查询订单的物流信息
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderTrackingResponse> getTrackingInfo(@PathVariable int orderId) {
        OrderTrackingResponse response = trackingService.getTrackingInfoByOrderId(orderId);
        return ResponseEntity.ok(response);
    }
}

