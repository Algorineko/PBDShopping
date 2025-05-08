package com.pbdcompany.controller;

import com.pbdcompany.entity.CustomerUserDetails;
import com.pbdcompany.service.ReturnExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/return-exchange")
public class ReturnExchangeController {

    @Autowired
    private ReturnExchangeService returnExchangeService;

    @PostMapping("/{orderId}/return")
    public ResponseEntity<?> applyReturn(
            String orderId,
            String reason,
            String logisticsCompany,
            String trackingNumber,
            @AuthenticationPrincipal CustomerUserDetails userDetails){
        String customerId = userDetails.getCustomerId();
        returnExchangeService.applyReturn(orderId, customerId, reason, logisticsCompany, trackingNumber);

        return ResponseEntity.ok("Return request submitted successfully");
    }

    @PostMapping("/{orderId}/exchange")
    public ResponseEntity<?> applyExchange(
            String orderId,
            String reason,
            String logisticsCompany,
            String trackingNumber,
            @AuthenticationPrincipal CustomerUserDetails userDetails){
        String customerId = userDetails.getCustomerId();
        returnExchangeService.applyExchange(orderId, customerId, reason, logisticsCompany, trackingNumber);

        return ResponseEntity.ok("Exchange request submitted successfully");
    }
}
