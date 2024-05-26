package com.warehousemanagement.controller;

import com.warehousemanagement.request.AddItemRequestDTO;
import com.warehousemanagement.request.DeleteItemRequestDTO;
import com.warehousemanagement.request.UpdateItemRequestDTO;
import com.warehousemanagement.response.AddItemResponseDTO;
import com.warehousemanagement.response.DeleteItemResponseDTO;
import com.warehousemanagement.response.LoadItemResponseDTO;
import com.warehousemanagement.service.InventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Transactional
@RequestMapping("/rest/inventory-items")
public class InventoryItemController {

    @Autowired
    private InventoryItemService inventoryItemService;

    @PostMapping("/add-item")
    @Secured("ROLE_WAREHOUSE_MANAGER")
    public ResponseEntity<AddItemResponseDTO> addItem(@Valid @RequestBody AddItemRequestDTO addItemRequestDTO){
        AddItemResponseDTO addItemResponseDTO = inventoryItemService.addItem(addItemRequestDTO);
        return new ResponseEntity<>(addItemResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @Secured("ROLE_WAREHOUSE_MANAGER")
    public ResponseEntity<DeleteItemResponseDTO> deleteItem(@RequestBody DeleteItemRequestDTO deleteItemRequestDTO){
        DeleteItemResponseDTO deleteItemResponseDTO = inventoryItemService.deleteItem(deleteItemRequestDTO);
        return new ResponseEntity<>(deleteItemResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/load/{itemId}")
    @Secured("ROLE_WAREHOUSE_MANAGER")
    public ResponseEntity<LoadItemResponseDTO> loadItem(@PathVariable("itemId") int itemId){
        LoadItemResponseDTO loadItemResponseDTO = inventoryItemService.loadItem(itemId);
        return new ResponseEntity<>(loadItemResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    @Secured("ROLE_WAREHOUSE_MANAGER")
    public ResponseEntity<String> updateItem(@RequestBody UpdateItemRequestDTO updateItemRequestDTO){
        String message = inventoryItemService.updateItem(updateItemRequestDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
