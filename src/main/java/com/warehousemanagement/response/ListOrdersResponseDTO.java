package com.warehousemanagement.response;

import com.warehousemanagement.pojo.Order;
import lombok.Data;

import java.util.List;

@Data
public class ListOrdersResponseDTO {
    private List<Order> orders;

    public ListOrdersResponseDTO(List<Order> orders) {
        this.orders = orders;
    }
}
