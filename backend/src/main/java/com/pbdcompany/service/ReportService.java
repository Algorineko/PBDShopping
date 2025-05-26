package com.pbdcompany.service;
import com.pbdcompany.dto.request.GenerateReportRequest;
import com.pbdcompany.dto.response.SalesReportResponse;
import com.pbdcompany.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private OrdersMapper ordersMapper;

    public List<SalesReportResponse> generateReport(GenerateReportRequest request) {
        return ordersMapper.generateSalesReport(
                request.getMerchantId(),
                request.getCategoryId(),
                request.getStartDate(),
                request.getEndDate()
        );
    }
}
