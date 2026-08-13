package com.emiyaconsulting.todo_list_api.service;

import com.emiyaconsulting.todo_list_api.exception.ItemNotFoundException;
import com.emiyaconsulting.todo_list_api.model.Item;
import com.emiyaconsulting.todo_list_api.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
    
    public ItemService(ItemRepository itemRepository, MongoTemplate mongoTemplate) {
        this.itemRepository = itemRepository;
        this.mongoTemplate = mongoTemplate;
    }
    
    public Item createItem(Item item) {
        Item savedItem = itemRepository.save(item);
        logger.info("Created item {} for owner {}", savedItem.getId(), savedItem.getOwner());
        return savedItem;
    }
    
    public Iterable<Item> getItems() {
        List<Item> items = itemRepository.findAll();
        List<Item> returnedItems = new ArrayList<>();
        
        for (Item item : items) {
            if (!item.isDeleted()) {
                returnedItems.add(item);
            }
        }
        
        logger.debug("Returning {} non-deleted items out of {} total", 
                returnedItems.size(), items.size());
        return returnedItems;
    }
    
    public Iterable<Item> getItemsByUser(String userName) {
        Query query = Query.query(new Criteria().andOperator(
                Criteria.where("owner").is(userName), 
                Criteria.where("deleted").is(false)
        ));
        List<Item> items = mongoTemplate.find(query, Item.class);
        logger.debug("Found {} items for owner {}", items.size(), userName);
        return items;
    }

    public Item findOneItem(String id) throws ItemNotFoundException {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(
                        String.format("(findOneItem: Lookup failed: No item with the id %s", id)));
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
            
            Item saved = itemRepository.save(existingItem);
            logger.info("Updated item {}", id);
            return saved;
        }
        throw new ItemNotFoundException(String.format("updateItem: Update failed: " +
                "No item with id %s", id));
    }
    
    // Set the Complete field on an existing item to true
    public Item completeItem(String id) throws ItemNotFoundException {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item existingItem = optionalItem.get();
            existingItem.setComplete(true);
            Item saved = itemRepository.save(existingItem);
            logger.info("Marked item {} complete", id);
            return saved;
        }
        throw new ItemNotFoundException(String.format("completeItem: Complete failed: " +
                "No item with the id %s", id));
    }
    
    // Performs a soft delete setting the deleted flag to true and setting deletedAt to the current datetime
    public Item deleteItem(String id) throws ItemNotFoundException {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item existingItem = optionalItem.get();
            existingItem.setDeleted(true);
            existingItem.setDeletedAt(Instant.now());
            
            Item saved = itemRepository.save(existingItem);
            logger.info("Set deleted flag to true item {}", id);
            return saved;
        }
        throw new ItemNotFoundException(String.format("deleteItem: Delete failed: " +
                "No item with the id %s", id));
    }
}
