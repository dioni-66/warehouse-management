package com.warehousemanagement.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Delivery {
    private LocalDate deliveryDate;
    private List<DeliveryTruck> deliveryTrucks;
}
