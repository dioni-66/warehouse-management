package com.warehousemanagement.response;

import com.warehousemanagement.pojo.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoadItemResponseDTO {
    private Item item;
}
