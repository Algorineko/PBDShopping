package com.pbdcompany.enums;


import lombok.Getter;

@Getter
public enum Status {
    PENDING("待处理"),
    NOT_SHIPPED("未发货"),
    IN_TRANSIT("运输中"),
    DELIVERED("已签收"),
    RETURNING("退货中"),
    RETURNED("已退货");

    private final String description;

    Status(String description) {
        this.description = description;
    }

}
