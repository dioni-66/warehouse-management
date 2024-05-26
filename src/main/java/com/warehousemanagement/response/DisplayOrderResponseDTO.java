package com.warehousemanagement.response;

import com.warehousemanagement.pojo.Order;
import lombok.Data;

@Data
public class DisplayOrderResponseDTO {
    private Order order;

    public DisplayOrderResponseDTO(Order order) {
        this.order = order;
    }
}
