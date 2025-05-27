package com.pbdcompany.enums;

public enum UserType {
    CUSTOMER("customer"),
    MERCHANT("merchant"),
    ADMIN("admin");
    private String value;
    UserType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
