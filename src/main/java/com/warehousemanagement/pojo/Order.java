package com.warehousemanagement.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Order {
    private int orderNumber;
    private LocalDate submittedDate;
    private LocalDate deadlineDate;
    private String orderStatus;
    private List<Item> orderItems;
}
