package com.emiyaconsulting.emiya_todo_list_api.service;

import com.emiyaconsulting.emiya_todo_list_api.model.Item;
import com.emiyaconsulting.emiya_todo_list_api.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }
    
    public Iterable<Item> getItems() {
        List<Item> items = itemRepository.findAll();
        List<Item> returnedItems = new ArrayList<>();
        
        for (Item item : items) {
            if (!item.isDeleted()) {
                returnedItems.add(item);
            }
        }
        
        return returnedItems;
    }

    public Item findOneItem(String id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        return optionalItem.orElse(null);
    }
    
    public Item updateItem(String id, Item updatedItem) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item existingItem = optionalItem.get();
            existingItem.setTitle(updatedItem.getTitle() != null ? updatedItem.getTitle() : optionalItem.get().getTitle());
            existingItem.setItemDescription(updatedItem.getItemDescription() != null ? updatedItem.getItemDescription() : optionalItem.get().getItemDescription());
            existingItem.setImportance(updatedItem.getImportance() != null ? updatedItem.getImportance() : optionalItem.get().getImportance());
            existingItem.setDue(updatedItem.getDue() != null ? updatedItem.getDue() : optionalItem.get().getDue());
            existingItem.setOwner(updatedItem.getOwner() != null ? updatedItem.getOwner() : optionalItem.get().getOwner());
            existingItem.setComplete(updatedItem.isComplete());
            
            return itemRepository.save(existingItem);
        }
        return null; // TODO: Add an exception for if item not found
    }
    
    // Performs a soft delete setting the deleted flag to true and setting deletedAt to the current datetime
    public Item deleteItem(String id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item existingItem = optionalItem.get();
            existingItem.setDeleted(true);
            existingItem.setDeletedAt(LocalDateTime.now());
            
            return itemRepository.save(existingItem);
        }
        return null; // TODO: Add an exception for if item not found
    }
}
