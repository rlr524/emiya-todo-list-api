package com.emiyaconsulting.emiya_todo_list_api.controller;

import com.emiyaconsulting.emiya_todo_list_api.model.Item;
import com.emiyaconsulting.emiya_todo_list_api.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class ItemController {
    private final ItemService itemService;
    
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }
    
    // Insert a single item
    @PostMapping("/item")
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {
        return ResponseEntity.ok(itemService.createItem(item));
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
