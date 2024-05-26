package com.warehousemanagement.entity;

import java.io.Serializable;

public class OrderItemsId implements Serializable {
    private int order;
    private int inventoryItem;

    public OrderItemsId() {
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(int inventoryItemId) {
        this.inventoryItem = inventoryItemId;
    }
}
