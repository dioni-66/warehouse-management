package com.warehousemanagement.pojo;

public enum OrderStatus {

    CREATED("CREATED"),
    AWAITING_APPROVAL("AWAITING_APPROVAL"),
    APPROVED("APPROVED"),
    UNDER_DELIVERY("UNDER_DELIVERY"),
    FULFILLED("FULFILLED"),
    CANCELLED("CANCELLED"),
    DECLINED("DECLINED");

    private String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
