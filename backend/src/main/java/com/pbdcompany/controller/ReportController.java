package com.pbdcompany.controller;

import com.pbdcompany.dto.request.GenerateReportRequest;
import com.pbdcompany.dto.response.SalesReportResponse;
import com.pbdcompany.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/sales")
    public ResponseEntity<List<SalesReportResponse>> generateSalesReport(@RequestBody GenerateReportRequest request) {
        List<SalesReportResponse> report = reportService.generateReport(request);
        return ResponseEntity.ok(report);
    }
}
