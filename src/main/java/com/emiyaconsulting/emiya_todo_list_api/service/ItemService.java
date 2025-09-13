package com.emiyaconsulting.emiya_todo_list_api.service;

import com.emiyaconsulting.emiya_todo_list_api.model.Item;
import com.emiyaconsulting.emiya_todo_list_api.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }
    
    public Iterable<Item> getItems() {
        return itemRepository.findAll();
    }
    
    public Item updateItem(String id, Item updatedItem) {
        Optional<Item> optionalItemItem = itemRepository.findById(id);
        if (optionalItemItem.isPresent()) {
            Item existingItem = optionalItemItem.get();
            existingItem.setTitle(updatedItem.getTitle());
            existingItem.setItemDescription(updatedItem.getItemDescription());
            existingItem.setImportance(updatedItem.getImportance());
            existingItem.setDue(updatedItem.getDue());
            existingItem.setOwner(updatedItem.getOwner());
            existingItem.setComplete(updatedItem.isComplete());
            
            return itemRepository.save(existingItem);
        }
        return null; // TODO: Add an exception for is item not found
    }
}
