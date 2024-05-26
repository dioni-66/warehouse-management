package com.warehousemanagement.pojo;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryTruck {
    private String licensePlate;
    private List<Integer> deliveryOrders;
}
