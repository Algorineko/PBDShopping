package com.pbdcompany.controller;

import com.pbdcompany.dto.response.OrderTrackingResponse;
import com.pbdcompany.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/order/{orderId}/tracking")
public class TrackingController {
    @Autowired
    private TrackingService trackingService;

    @GetMapping
    public OrderTrackingResponse getTrackingInfo(@PathVariable("orderId") int orderId) {
        return trackingService.getTrackingInfo(orderId);
    }

}
