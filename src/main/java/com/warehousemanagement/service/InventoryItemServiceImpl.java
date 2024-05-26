package com.warehousemanagement.service;

import com.warehousemanagement.entity.InventoryItemEntity;
import com.warehousemanagement.pojo.Item;
import com.warehousemanagement.repository.InventoryItemRepository;
import com.warehousemanagement.request.AddItemRequestDTO;
import com.warehousemanagement.request.DeleteItemRequestDTO;
import com.warehousemanagement.request.UpdateItemRequestDTO;
import com.warehousemanagement.response.AddItemResponseDTO;
import com.warehousemanagement.response.DeleteItemResponseDTO;
import com.warehousemanagement.response.LoadItemResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InventoryItemServiceImpl implements InventoryItemService {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    public AddItemResponseDTO addItem(AddItemRequestDTO addItemRequestDTO) {

        InventoryItemEntity existingItem = inventoryItemRepository.findByName(addItemRequestDTO.getName());
        if (existingItem != null) {
            return new AddItemResponseDTO(String.format("Item '%s' already exists in inventory", addItemRequestDTO.getName()));
        }

        InventoryItemEntity inventoryItemEntity = new InventoryItemEntity();
        inventoryItemEntity.setName(addItemRequestDTO.getName());
        inventoryItemEntity.setPrice(addItemRequestDTO.getPrice());
        inventoryItemEntity.setQuantity(addItemRequestDTO.getQuantity());
        inventoryItemRepository.save(inventoryItemEntity);

        return new AddItemResponseDTO("Item added successfully to inventory");
    }

    @Override
    public DeleteItemResponseDTO deleteItem(DeleteItemRequestDTO deleteItemRequestDTO) {
        InventoryItemEntity inventoryItemEntity = inventoryItemRepository.findById(deleteItemRequestDTO.getItemId()).orElseThrow(() -> new IllegalArgumentException("Invalid item id"));
        inventoryItemRepository.delete(inventoryItemEntity);
        return new DeleteItemResponseDTO("Item deleted successfully");
    }

    @Override
    public LoadItemResponseDTO loadItem(int itemId) {
        InventoryItemEntity inventoryItemEntity = inventoryItemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item id"));

        //convert to pojo and return the response
        Item item = convertItemEntityToPojo(inventoryItemEntity);
        return new LoadItemResponseDTO(item);
    }

    @Override
    public String updateItem(UpdateItemRequestDTO updateItemRequestDTO) {
        InventoryItemEntity inventoryItemEntity = inventoryItemRepository.findById(updateItemRequestDTO.getItemId()).orElseThrow(() -> new IllegalArgumentException("Invalid item id"));
        inventoryItemEntity.setQuantity(updateItemRequestDTO.getItem().getQuantity());
        inventoryItemEntity.setPrice(updateItemRequestDTO.getItem().getPrice());
        inventoryItemEntity.setName(updateItemRequestDTO.getItem().getName());
        inventoryItemRepository.save(inventoryItemEntity);
        return "Item updated successfully";
    }

    private Item convertItemEntityToPojo(InventoryItemEntity inventoryItemEntity) {
        Item item = new Item();
        item.setQuantity(inventoryItemEntity.getQuantity());
        item.setName(inventoryItemEntity.getName());
        item.setPrice(inventoryItemEntity.getPrice());
        return item;
    }

}
