package com.warehousemanagement.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Item {
    private String name;
    private int quantity;
    private BigDecimal price;
}
