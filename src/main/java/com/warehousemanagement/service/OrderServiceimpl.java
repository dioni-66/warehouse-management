package com.warehousemanagement.service;

import com.warehousemanagement.entity.*;
import com.warehousemanagement.pojo.DeliveryTruck;
import com.warehousemanagement.pojo.Item;
import com.warehousemanagement.pojo.Order;
import com.warehousemanagement.pojo.OrderStatus;
import com.warehousemanagement.repository.*;
import com.warehousemanagement.request.ScheduleDeliveryRequestDTO;
import com.warehousemanagement.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceimpl implements OrderService {

    @Autowired
    private OrdersRepository ordersRepository;
    
    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DeliveryRepository deliveryRepository;

    public CreateOrderResponseDTO createOrder(HttpServletRequest httpServletRequest) {
        OrdersEntity ordersEntity = new OrdersEntity();
        LocalDate currentDate = LocalDate.now();
        //orders should be delivered within one day
        LocalDate deadlineDate = currentDate.plusDays(1);
        ordersEntity.setSubmittedDate(currentDate);
        ordersEntity.setDeadlineDate(deadlineDate);
        ordersEntity.setUser(userService.getLoggedInUser(httpServletRequest));
        ordersEntity.setOrderStatus(orderStatusRepository.findByStatus(OrderStatus.CREATED.getValue()));

        ordersRepository.save(ordersEntity);
        return new CreateOrderResponseDTO("Order created successfully");
    }

    @Override
    public AddItemResponseDTO addItemToOrder(int orderId, int itemId, int quantity) {
        InventoryItemEntity inventoryItemEntity = inventoryItemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item id"));
        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order number"));
        checkItemAvailability(inventoryItemEntity.getQuantity(), quantity);

        OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
        orderItemsEntity.setOrder(ordersEntity);
        orderItemsEntity.setInventoryItem(inventoryItemEntity);
        orderItemsEntity.setRequestedQuantity(quantity);
        orderItemsRepository.save(orderItemsEntity);

        return new AddItemResponseDTO("Item added successfully to order");
    }

    private void checkItemAvailability(int quantityInInventory, int requestedQuantity) {
        if (requestedQuantity > quantityInInventory) {
            throw new IllegalArgumentException("Requested quantity not available in inventory");
        }
    }

    @Override
    public String removeItemFromOrder(int orderId, int itemId) {
        try {
            OrderItemsEntity orderItemEntity = orderItemsRepository.getByOrder_OrderNumberAndInventoryItem_Id(orderId, itemId);
            orderItemsRepository.delete(orderItemEntity);
            return "Item removed successfully";
        } catch (Exception e){
            throw new IllegalArgumentException("Item not present in the order.");
        }
    }

    @Override
    public String updateItemQuantity(int orderId, int itemId, int quantity) {
        InventoryItemEntity inventoryItemEntity = inventoryItemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item id"));
        checkItemAvailability(inventoryItemEntity.getQuantity(), quantity);

        OrderItemsEntity orderItemEntity = orderItemsRepository.getByOrder_OrderNumberAndInventoryItem_Id(orderId, itemId);
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative. Please provide a positive number!");
        } else {
            orderItemEntity.setRequestedQuantity(quantity);
            orderItemsRepository.save(orderItemEntity);
        }
        return "Quantity updated successfully";
    }


    @Override
    public DisplayOrderResponseDTO displayOrder(Integer orderId) {
        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order id"));
        Order order = convertOrdersToPojo(List.of(ordersEntity)).get(0);
        return new DisplayOrderResponseDTO(order);
    }

    @Override
    public ApproveOrderResponseDTO approveOrder(Integer orderId, String orderStatus) {

        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order id"));

        if (!Arrays.stream(OrderStatus.values()).anyMatch(enumVal -> enumVal.getValue().equals(orderStatus))) {
            return new ApproveOrderResponseDTO("Invalid order status passed as input argument");
        }

        if (!ordersEntity.getOrderStatus().getStatus().equals(OrderStatus.AWAITING_APPROVAL.getValue())) {
            return new ApproveOrderResponseDTO("Invalid order status. You cannot APPROVE/DECLINE it!");
        } else {
            OrderStatusEntity newStatus = orderStatusRepository.findByStatus(orderStatus);
            ordersEntity.setOrderStatus(newStatus);
            ordersRepository.save(ordersEntity);
            if (orderStatus.equals(OrderStatus.DECLINED.getValue())) {
                return new ApproveOrderResponseDTO("Order declined. Please submit a reason for declining!");
            } else {
                return new ApproveOrderResponseDTO("Order approved!");
            }
        }
    }

    @Override
    public String cancelOrder(int orderId) {
        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order id"));

        List<String> invalidOrderStatuses = List.of(OrderStatus.FULFILLED.getValue(), OrderStatus.UNDER_DELIVERY.getValue(), OrderStatus.CANCELLED.getValue());
        if (invalidOrderStatuses.contains(ordersEntity.getOrderStatus().getStatus())) {
            return "Order cannot be cancelled. Invalid order status";
        } else {
            OrderStatusEntity cancelledStatus = orderStatusRepository.findByStatus(OrderStatus.CANCELLED.getValue());
            ordersEntity.setOrderStatus(cancelledStatus);
            ordersRepository.save(ordersEntity);
            return "Order cancelled successfully";
        }
    }

    @Override
    public String submitOrder(int orderId) {
        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order id"));

        List<String> validOrderStatuses = List.of(OrderStatus.CREATED.getValue(), OrderStatus.DECLINED.getValue());
        if (validOrderStatuses.contains(ordersEntity.getOrderStatus().getStatus())) {
            //set order status to awaiting approval only if status is CREATED or DECLINED
            OrderStatusEntity awaitingApprovalStatus = orderStatusRepository.findByStatus(OrderStatus.AWAITING_APPROVAL.getValue());
            ordersEntity.setOrderStatus(awaitingApprovalStatus);
            ordersRepository.save(ordersEntity);
            return "Order submitted successfully";
        } else {
            return "Order cannot be submitted. Invalid order status";
        }
    }

    @Override
    public ListOrdersResponseDTO listClientOrders(String orderStatus, HttpServletRequest httpServletRequest) {
        UserEntity loggedInUser = userService.getLoggedInUser(httpServletRequest);

        //if filtered by order status use filter query
        //otherwise filter only by user id
        List<OrdersEntity> userOrders;

        if (orderStatus != null) {
            userOrders = ordersRepository.getAllByUser_IdAndOrderStatus_Status(loggedInUser.getId(), orderStatus);
        } else {
            userOrders = ordersRepository.getAllByUser_Id(loggedInUser.getId());
        }

        List<Order> orderList = convertOrdersToPojo(userOrders);
        return new ListOrdersResponseDTO(orderList);
    }

    @Override
    public ListOrdersResponseDTO viewAllOrders(String orderStatus) {
        //if filtered by order status use filter query
        List<OrdersEntity> userOrders;
        if (orderStatus != null) {
            userOrders = ordersRepository.getAllByOrderStatus_StatusOrderBySubmittedDateDesc(orderStatus);
        } else {
            userOrders = ordersRepository.findAllByOrderBySubmittedDateDesc();
        }

        List<Order> orderList = convertOrdersToPojo(userOrders);
        return new ListOrdersResponseDTO(orderList);
    }

    @Override
    public String scheduleDelivery(ScheduleDeliveryRequestDTO scheduleDeliveryRequestDTO) {
        String errorMessage = validateDeliveryDate(scheduleDeliveryRequestDTO.getDelivery().getDeliveryDate());
        if (errorMessage != null) {
            return errorMessage;
        }

        errorMessage = validateOrderDeadlineDate(scheduleDeliveryRequestDTO);
        if (errorMessage != null) {
            return errorMessage;
        }

        errorMessage = validateTruckAvailability(scheduleDeliveryRequestDTO);
        if (errorMessage != null) {
            return errorMessage;
        }
        
        saveValidDelivery(scheduleDeliveryRequestDTO);
        
        return "Delivery scheduled successfully";
    }

    @Override
    public String fulfillOrder(Integer orderNumber) {
        OrdersEntity ordersEntity = ordersRepository.findById(orderNumber).orElseThrow(() -> new IllegalArgumentException("Invalid order number"));

        if (!ordersEntity.getOrderStatus().getStatus().equals(OrderStatus.UNDER_DELIVERY.getValue())) {
            return "Invalid order status. You cannot fulfill it!";
        }

        OrderStatusEntity fulfilledStatus = orderStatusRepository.findByStatus(OrderStatus.FULFILLED.getValue());
        ordersEntity.setOrderStatus(fulfilledStatus);
        ordersRepository.save(ordersEntity);
        return "Order fulfilled successfully.";
    }

    private void saveValidDelivery(ScheduleDeliveryRequestDTO scheduleDeliveryRequestDTO) {
        DeliveryEntity deliveryEntity = new DeliveryEntity();
        deliveryEntity.setDeliveryDate(scheduleDeliveryRequestDTO.getDelivery().getDeliveryDate());
        deliveryEntity.setDeliveryTrucks(new ArrayList<>());
        for (DeliveryTruck deliveryTruck : scheduleDeliveryRequestDTO.getDelivery().getDeliveryTrucks()) {
            DeliveryTruckEntity deliveryTruckEntity = new DeliveryTruckEntity();
            deliveryTruckEntity.setTruck(truckRepository.getByLicensePlateEquals(deliveryTruck.getLicensePlate()));
            deliveryTruckEntity.setDeliveryTruckOrders(new ArrayList<>());
            for (Integer orderNumber : deliveryTruck.getDeliveryOrders()) {
                OrdersEntity ordersEntity = ordersRepository.findById(orderNumber).orElse(null);
                //set order status to UNDER_DELIVERY and update inventory items before saving
                ordersEntity.setOrderStatus(orderStatusRepository.findByStatus(OrderStatus.UNDER_DELIVERY.getValue()));
                reduceInventoryItems(ordersEntity);
                DeliveryTruckOrderEntity deliveryTruckOrderEntity = new DeliveryTruckOrderEntity();
                deliveryTruckOrderEntity.setOrder(ordersEntity);
                deliveryTruckOrderEntity.setDeliveryTruck(deliveryTruckEntity);
                deliveryTruckEntity.getDeliveryTruckOrders().add(deliveryTruckOrderEntity);
            }
            deliveryEntity.getDeliveryTrucks().add(deliveryTruckEntity);
        }
        deliveryRepository.save(deliveryEntity);
    }

    private void reduceInventoryItems(OrdersEntity ordersEntity) {
        for (OrderItemsEntity orderItemsEntity : ordersEntity.getOrderItems()) {
            InventoryItemEntity inventoryItemEntity = inventoryItemRepository.findById(orderItemsEntity.getInventoryItem().getId()).orElse(null);
            inventoryItemEntity.setQuantity(inventoryItemEntity.getQuantity() - orderItemsEntity.getRequestedQuantity());
            inventoryItemRepository.save(inventoryItemEntity);
        }
    }

    private String validateOrderDeadlineDate(ScheduleDeliveryRequestDTO scheduleDeliveryRequestDTO) {
        List<Integer> orderIds = scheduleDeliveryRequestDTO.getDelivery().getDeliveryTrucks().stream()
                .flatMap(deliveryTruck -> deliveryTruck.getDeliveryOrders().stream())
                .distinct()
                .collect(Collectors.toList());
        List<OrdersEntity> orderEntities = ordersRepository.findAllById(orderIds);
        for (OrdersEntity ordersEntity : orderEntities) {
            if (!ordersEntity.getDeadlineDate().isEqual(scheduleDeliveryRequestDTO.getDelivery().getDeliveryDate())) {
                //if manager requests a delivery date for the order that is not the same as the predefined order deadline date
                return String.format("Invalid delivery date %tD for order number: %d", scheduleDeliveryRequestDTO.getDelivery().getDeliveryDate(), ordersEntity.getOrderNumber());
            }
        }
        return null;
    }

    private String validateTruckAvailability(ScheduleDeliveryRequestDTO scheduleDeliveryRequestDTO) {

        if (scheduleDeliveryRequestDTO.getDelivery().getDeliveryTrucks() == null || scheduleDeliveryRequestDTO.getDelivery().getDeliveryTrucks().isEmpty()) {
            return "Please specify at minimum 1 truck for the delivery.";
        }

        //collect all delivery truck license plates that are occupied on the requested delivery date
        List<DeliveryEntity> deliveryEntities = deliveryRepository.getAllByDeliveryDateIs(scheduleDeliveryRequestDTO.getDelivery().getDeliveryDate());
        List<String> licensePlates = deliveryEntities.stream()
                .flatMap(deliveryEntity -> deliveryEntity.getDeliveryTrucks().stream())
                .map(deliveryTruckEntity -> deliveryTruckEntity.getTruck().getLicensePlate())
                .collect(Collectors.toList());

        //check if a particular truck is not available on the delivery date scheduled by the warehouse manager
        for (DeliveryTruck deliveryTruck : scheduleDeliveryRequestDTO.getDelivery().getDeliveryTrucks()) {
            if (licensePlates.contains(deliveryTruck.getLicensePlate())) {
                return String.format("Truck with license plate `%s` is not available on %tD", deliveryTruck.getLicensePlate(), scheduleDeliveryRequestDTO.getDelivery().getDeliveryDate());
            }
        }

        //check the number of items a specific truck is carrying
        for (DeliveryTruck deliveryTruck : scheduleDeliveryRequestDTO.getDelivery().getDeliveryTrucks()) {
            List<OrderItemsEntity> requestedOrdersList = orderItemsRepository.getByOrder_OrderNumberIn(deliveryTruck.getDeliveryOrders());
            int sumOfItems = 0;
            for (OrderItemsEntity requestedOrder : requestedOrdersList) {
                //add requested quantity for each item present in the order
                sumOfItems += requestedOrder.getRequestedQuantity();
            }
            if (sumOfItems > 10) {
                return String.format("Truck with license plate '%s' cannot carry %d items. Max allowed items: 10",deliveryTruck.getLicensePlate(), sumOfItems);
            }
        }

        return null;
    }

    private String validateDeliveryDate(LocalDate deliveryDate) {
        if (deliveryDate == null) {
            return "Delivery date must be specified.";
        } else if (LocalDate.now().isAfter(deliveryDate)) {
            return "Invalid delivery date. Delivery date cannot be set in the past.";
        } else if (deliveryDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            return "Deliveries cannot be scheduled on Sundays";
        }
        return null;
    }

    private List<Order> convertOrdersToPojo(List<OrdersEntity> userOrders) {
        List<Order> orderList = new ArrayList<>();
        for (OrdersEntity userOrder : userOrders) {
            Order orderPojo = new Order();
            orderPojo.setOrderNumber(userOrder.getOrderNumber());
            orderPojo.setOrderStatus(userOrder.getOrderStatus().getStatus());
            orderPojo.setDeadlineDate(userOrder.getDeadlineDate());
            orderPojo.setSubmittedDate(userOrder.getSubmittedDate());
            List<Item> orderItems = convertOrderItemsToPojo(userOrder.getOrderItems());
            orderPojo.setOrderItems(orderItems);

            orderList.add(orderPojo);
        }
        return orderList;
    }

    private List<Item> convertOrderItemsToPojo(List<OrderItemsEntity> orderItems) {
        List<Item> itemList = new ArrayList<>();
        for (OrderItemsEntity orderItem : orderItems) {
            Item itemPojo = new Item();
            itemPojo.setName(orderItem.getInventoryItem().getName());
            itemPojo.setPrice(orderItem.getInventoryItem().getPrice());
            itemPojo.setQuantity(orderItem.getRequestedQuantity());
            itemList.add(itemPojo);
        }
        return itemList;
    }

}
