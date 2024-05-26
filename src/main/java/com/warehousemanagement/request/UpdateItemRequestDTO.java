package com.warehousemanagement.request;

import com.warehousemanagement.pojo.Item;
import lombok.Data;

@Data
public class UpdateItemRequestDTO {
    private int itemId;
    private Item item;
}
