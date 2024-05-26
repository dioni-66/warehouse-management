package com.warehousemanagement.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "order_items")
@IdClass(OrderItemsId.class)
public class OrderItemsEntity implements Serializable {

    private Integer requestedQuantity;

    private OrdersEntity order;
    private InventoryItemEntity inventoryItem;

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_number")
    public OrdersEntity getOrder() {
        return order;
    }

    public void setOrder(OrdersEntity orderByOrderId) {
        this.order = orderByOrderId;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    public InventoryItemEntity getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(InventoryItemEntity inventoryItemByItemId) {
        this.inventoryItem = inventoryItemByItemId;
    }

    @Basic
    @Column(name = "requested_quantity")
    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(Integer requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemsEntity that = (OrderItemsEntity) o;
        return Objects.equals(requestedQuantity, that.requestedQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestedQuantity);
    }

}
