package com.warehousemanagement.service;

import com.warehousemanagement.request.AddItemRequestDTO;
import com.warehousemanagement.request.DeleteItemRequestDTO;
import com.warehousemanagement.request.UpdateItemRequestDTO;
import com.warehousemanagement.response.AddItemResponseDTO;
import com.warehousemanagement.response.DeleteItemResponseDTO;
import com.warehousemanagement.response.LoadItemResponseDTO;

public interface InventoryItemService {

    AddItemResponseDTO addItem(AddItemRequestDTO addItemRequestDTO);

    DeleteItemResponseDTO deleteItem(DeleteItemRequestDTO deleteItemRequestDTO);

    LoadItemResponseDTO loadItem(int itemId);

    String updateItem(UpdateItemRequestDTO updateItemRequestDTO);
}
