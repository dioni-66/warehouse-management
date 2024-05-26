package com.warehousemanagement.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "delivery_truck_order")
public class DeliveryTruckOrderEntity implements Serializable {
    private DeliveryTruckEntity deliveryTruck;
    private OrdersEntity order;

    @Id
    @ManyToOne
    @JoinColumn(name = "delivery_truck_id", referencedColumnName = "id")
    public DeliveryTruckEntity getDeliveryTruck() {
        return deliveryTruck;
    }

    public void setDeliveryTruck(DeliveryTruckEntity deliveryTruckByDeliveryTruckId) {
        this.deliveryTruck = deliveryTruckByDeliveryTruckId;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_number")
    public OrdersEntity getOrder() {
        return order;
    }

    public void setOrder(OrdersEntity orderByOrderId) {
        this.order = orderByOrderId;
    }
}
