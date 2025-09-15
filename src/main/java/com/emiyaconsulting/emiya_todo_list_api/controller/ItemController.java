package com.emiyaconsulting.emiya_todo_list_api.controller;

import com.emiyaconsulting.emiya_todo_list_api.model.Item;
import com.emiyaconsulting.emiya_todo_list_api.service.ItemService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {
    private final ItemService itemService;
    
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }
    
    // Insert a single item
    // The taint analysis warning is suppressed because we are sanitizing the input
    // using StringEscapeUtils.escapeHtml4 to prevent XSS attacks.
    @SuppressWarnings("JvmTaintAnalysis")
    @PostMapping("/item")
    public ResponseEntity<Item> saveItem(@RequestBody Item item) {
        if (item != null) {
            if (item.getItemDescription() != null) {
                item.setItemDescription(StringEscapeUtils.escapeHtml4(item.getItemDescription()));
            }
            if (item.getTitle() != null) {
                item.setTitle(StringEscapeUtils.escapeHtml4(item.getTitle()));
            }
            if (item.getOwner() != null) {
                item.setOwner(StringEscapeUtils.escapeHtml4(item.getOwner()));
            }
            if (item.getDue() != null) {
                item.setDue(StringEscapeUtils.escapeHtml4(item.getDue()));
            }
            if (item.getImportance() != null) {
                item.setImportance(StringEscapeUtils.escapeHtml4(item.getImportance()));
            }
        }

        Item savedItem = itemService.saveItem(item);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }
    
    // Return all items
    @GetMapping("/items")
    public Iterable<Item> getItems() {
        return itemService.getItems();
    }
    
    @GetMapping("/item/{id}")
    public Item getItem(@PathVariable String id) {
        return itemService.findOneItem(id);
    }
    
    @PutMapping("/item/{id}")
    public ResponseEntity<Item> updatedItem(@PathVariable String id, @RequestBody Item itemDetails) {
        Item updatedItem = itemService.updateItem(id, itemDetails);
        if (updatedItem != null) {
            return ResponseEntity.ok(updatedItem);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/item/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable String id) {
        Item deletedItem = itemService.deleteItem(id);
        if (deletedItem != null) {
            return ResponseEntity.ok(deletedItem);
        }
        return ResponseEntity.notFound().build();
    }
    
    
    // Healthcheck
    @GetMapping("/")
    public String healthCheck() {
        return "OK";
    }
}
