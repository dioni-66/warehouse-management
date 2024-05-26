package com.warehousemanagement.controller;

import com.warehousemanagement.request.ListOrdersRequestDTO;
import com.warehousemanagement.request.ScheduleDeliveryRequestDTO;
import com.warehousemanagement.response.*;
import com.warehousemanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Transactional
@RequestMapping("/rest/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create-order")
    @Secured("ROLE_CLIENT")
    public ResponseEntity<CreateOrderResponseDTO> createOrder(HttpServletRequest httpServletRequest){
    CreateOrderResponseDTO createOrderResponseDTO = orderService.createOrder(httpServletRequest);
    return new ResponseEntity<>(createOrderResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/add-item/{orderId}/{itemId}/{quantity}")
    @Secured("ROLE_CLIENT")
    public ResponseEntity<AddItemResponseDTO> addItemToOrder(@PathVariable("orderId") int orderId, @PathVariable("itemId") int itemId,
                                                             @PathVariable("quantity") int quantity){
        AddItemResponseDTO addItemResponseDTO = orderService.addItemToOrder(orderId, itemId, quantity);
        return new ResponseEntity<>(addItemResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/remove-item/{orderId}/{itemId}")
    @Secured("ROLE_CLIENT")
    public ResponseEntity<String> removeItemFromOrder(@PathVariable("orderId") int orderId, @PathVariable("itemId") int itemId){
        String message = orderService.removeItemFromOrder(orderId, itemId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/update-item-quantity/{orderId}/{itemId}/{quantity}")
    @Secured("ROLE_CLIENT")
    public ResponseEntity<String> updateItemQuantity(@PathVariable("orderId") int orderId, @PathVariable("itemId") int itemId,
                                                     @PathVariable("quantity") int quantity){
        String message = orderService.updateItemQuantity(orderId, itemId, quantity);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/cancel-order/{orderId}")
    @Secured("ROLE_CLIENT")
    public ResponseEntity<String> cancelOrder(@PathVariable("orderId") int orderId){
        String message = orderService.cancelOrder(orderId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/submit-order/{orderId}")
    @Secured("ROLE_CLIENT")
    public ResponseEntity<String> submitOrder(@PathVariable("orderId") int orderId){
        String message = orderService.submitOrder(orderId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/list-orders")
    @Secured("ROLE_CLIENT")
    public ResponseEntity<ListOrdersResponseDTO> listClientOrders(@RequestBody ListOrdersRequestDTO listOrdersRequestDTO, HttpServletRequest httpServletRequest){
        ListOrdersResponseDTO listOrdersResponseDTO = orderService.listClientOrders(listOrdersRequestDTO.getOrderStatus(), httpServletRequest);
        return new ResponseEntity<>(listOrdersResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/view-all-orders")
    @Secured("ROLE_WAREHOUSE_MANAGER")
    public ResponseEntity<ListOrdersResponseDTO> viewAllOrders(@RequestBody ListOrdersRequestDTO listOrdersRequestDTO){
        ListOrdersResponseDTO listOrdersResponseDTO = orderService.viewAllOrders(listOrdersRequestDTO.getOrderStatus());
        return new ResponseEntity<>(listOrdersResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/display-order/{orderId}")
    @Secured("ROLE_WAREHOUSE_MANAGER")
    public ResponseEntity<DisplayOrderResponseDTO> displayOrder(@PathVariable("orderId") Integer orderId){
        DisplayOrderResponseDTO displayOrderResponseDTO = orderService.displayOrder(orderId);
        return new ResponseEntity<>(displayOrderResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/approve-order/{orderId}/{orderStatus}")
    @Secured("ROLE_WAREHOUSE_MANAGER")
    public ResponseEntity<ApproveOrderResponseDTO> approveOrder(@PathVariable("orderId") Integer orderId, @PathVariable("orderStatus") String orderStatus){
        ApproveOrderResponseDTO approveOrderResponseDTO = orderService.approveOrder(orderId, orderStatus);
        return new ResponseEntity<>(approveOrderResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/schedule-delivery")
    @Secured("ROLE_WAREHOUSE_MANAGER")
    public ResponseEntity<String> scheduleDelivery(@RequestBody ScheduleDeliveryRequestDTO scheduleDeliveryRequestDTO){
        String message = orderService.scheduleDelivery(scheduleDeliveryRequestDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/fulfill-order/{orderNumber}")
    @Secured("ROLE_WAREHOUSE_MANAGER")
    public ResponseEntity<String> fulfillOrder(@PathVariable("orderNumber") Integer orderNumber){
        String message = orderService.fulfillOrder(orderNumber);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
