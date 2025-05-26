package com.pbdcompany.dto.request;


public class UpdateLogisticsRequest {
    private String logisticsCompany; // 物流公司名称
    private String trackingNumber;   // 物流单号

    // 获取物流公司名称
    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    // 设置物流公司名称
    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    // 获取物流单号
    public String getTrackingNumber() {
        return trackingNumber;
    }

    // 设置物流单号
    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
}
