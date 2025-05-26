package com.pbdcompany.controller;

import com.pbdcompany.dto.request.UpdateLogisticsRequest;
import com.pbdcompany.dto.response.LogisticsInfoResponse;
import com.pbdcompany.service.LogisticsinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchant/logistics")
public class LogisticsController {

    @Autowired
    private LogisticsinfoService logisticsinfoService;

    // 查询物流信息
    @GetMapping("/{orderItemId}")
    public ResponseEntity<LogisticsInfoResponse> getLogisticsInfo(@PathVariable int orderItemId) {
        LogisticsInfoResponse response = logisticsinfoService.getTrackingInfoByOrderItemId(orderItemId);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    // 更新物流信息
    @PutMapping("/{orderItemId}")
    public ResponseEntity<String> updateLogisticsInfo(
            @PathVariable int orderItemId,
            @RequestBody UpdateLogisticsRequest request) {
        // 假设此处调用第三方物流接口进行更新
        // 示例逻辑（实际应与物流系统对接）
        System.out.println("更新订单项 " + orderItemId + " 的物流信息: " +
                request.getLogisticsCompany() + " - " + request.getTrackingNumber());
        return ResponseEntity.ok("物流信息已提交至物流子系统");
    }
}
