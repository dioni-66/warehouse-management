package com.warehousemanagement.service;

import com.warehousemanagement.request.ScheduleDeliveryRequestDTO;
import com.warehousemanagement.response.*;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {

    CreateOrderResponseDTO createOrder(HttpServletRequest httpServletRequest);

    AddItemResponseDTO addItemToOrder(int orderId, int itemId, int quantity);

    String removeItemFromOrder(int orderId, int itemId);

    String updateItemQuantity(int orderId, int itemId, int quantity);

    String cancelOrder(int orderId);

    String submitOrder(int orderId);

    ListOrdersResponseDTO listClientOrders(String orderStatus, HttpServletRequest httpServletRequest);

    DisplayOrderResponseDTO displayOrder(Integer orderId);

    ApproveOrderResponseDTO approveOrder(Integer orderId, String orderStatus);

    ListOrdersResponseDTO viewAllOrders(String orderStatus);

    String scheduleDelivery(ScheduleDeliveryRequestDTO scheduleDeliveryRequestDTO);

    String fulfillOrder(Integer orderNumber);
}
