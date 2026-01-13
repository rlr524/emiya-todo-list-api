package com.emiyaconsulting.emiya_todo_list_api.service;

import com.emiyaconsulting.emiya_todo_list_api.exception.ItemNotFoundException;
import com.emiyaconsulting.emiya_todo_list_api.model.Item;
import com.emiyaconsulting.emiya_todo_list_api.repository.ItemRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final MongoTemplate mongoTemplate;
    
    public ItemService(ItemRepository itemRepository, MongoTemplate mongoTemplate) {
        this.itemRepository = itemRepository;
        this.mongoTemplate = mongoTemplate;
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
    
    public Iterable<Item> getItemsByUser(String userId) {
        Query query = Query.query(new Criteria().andOperator(
                Criteria.where("owner").is(userId), 
                Criteria.where("deleted").is(false)
        ));
        return mongoTemplate.find(query, Item.class);
    }

    public Item findOneItem(String id) throws ItemNotFoundException {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(
                        String.format("No item with the id %s is available", id)));
    }
    
    public Item updateItem(String id, Item updatedItem) throws ItemNotFoundException {
        Optional<Item> optionalItem = itemRepository.findById(id);
        
        if (optionalItem.isPresent()) {
            Item existingItem = optionalItem.get();
            existingItem.setTitle(updatedItem.getTitle() != null 
                    ? updatedItem.getTitle() 
                    : optionalItem.get().getTitle());
            existingItem.setItemDescription(updatedItem.getItemDescription() != null
                    ? updatedItem.getItemDescription() 
                    : optionalItem.get().getItemDescription());
            existingItem.setImportance(updatedItem.getImportance() != null 
                    ? updatedItem.getImportance() 
                    : optionalItem.get().getImportance());
            existingItem.setDue(updatedItem.getDue() != null 
                    ? updatedItem.getDue() 
                    : optionalItem.get().getDue());
            existingItem.setOwner(updatedItem.getOwner() != null 
                    ? updatedItem.getOwner() 
                    : optionalItem.get().getOwner());
            existingItem.setComplete(updatedItem.isComplete());
            
            return itemRepository.save(existingItem);
        }
        throw new ItemNotFoundException(String.format("No item with id %s is available", id));
    }
    
    // Performs a soft delete setting the deleted flag to true and setting deletedAt to the current datetime
    public Item deleteItem(String id) throws ItemNotFoundException {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item existingItem = optionalItem.get();
            existingItem.setDeleted(true);
            existingItem.setDeletedAt(Instant.now());
            
            return itemRepository.save(existingItem);
        }
        throw new ItemNotFoundException(String.format("No item with the id %s is available", id));
    }
}
