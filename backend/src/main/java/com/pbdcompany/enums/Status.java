package com.pbdcompany.enums;


import lombok.Getter;

@Getter
public enum Status {
    PAID("已付款"),
    PENDING("待支付"),
    NOT_SHIPPED("未发货"),
    SHIPPED("已发货"),
    IN_TRANSIT("运输中"),
    DELIVERED("已签收"),
    RETURNING("退货中"),
    RETURNED("已退货"),
    COMPLETED("已完成"),
    CANCELLED("已取消");
    private final String description;

    Status(String description) {
        this.description = description;
    }

}
