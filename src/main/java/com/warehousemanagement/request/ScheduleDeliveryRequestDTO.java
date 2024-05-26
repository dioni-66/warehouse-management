package com.warehousemanagement.request;

import com.warehousemanagement.pojo.Delivery;
import lombok.Data;

@Data
public class ScheduleDeliveryRequestDTO {
    private Delivery delivery;
}
