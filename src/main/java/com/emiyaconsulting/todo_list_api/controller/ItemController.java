package com.emiyaconsulting.todo_list_api.controller;

import com.emiyaconsulting.todo_list_api.model.Item;
import com.emiyaconsulting.todo_list_api.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Validated
public class ItemController {
    private final ItemService itemService;
    
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }
    
    // Insert a single item
    @PostMapping("/item")
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item, Principal principal) {
        item.setOwner(principal.getName());
        return ResponseEntity.ok(itemService.createItem(item));
    }
    
    // Return all items
    @GetMapping("/items")
    public Iterable<Item> getItems() {
        return itemService.getItems();
    }
    
    // Return all items by user id
    @GetMapping("/items/{id}")
    public Iterable<Item> getItemsByUser(@PathVariable String id) {
        return itemService.getItemsByUser(id);
    }
    
    // Return all items for the logged-in user
    @GetMapping("/items/mine")
    public ResponseEntity<Iterable<Item>> getMyItems(Principal principal) {
        return ResponseEntity.ok(itemService.getItemsByUser(principal.getName()));
    }
    
    // Return one item by the item's id
    @GetMapping("/item/{id}")
    public Item getItem(@PathVariable String id) {
        return itemService.findOneItem(id);
    }
    
    // Update one or more fields on an item by item id
    @PutMapping("/item/{id}")
    public ResponseEntity<Item> updatedItem(@PathVariable String id, @RequestBody Item itemDetails) {
        Item updatedItem = itemService.updateItem(id, itemDetails);
        if (updatedItem != null) {
            return ResponseEntity.ok(updatedItem);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Mark one item as completed by item id
    @PatchMapping("/item/{id}")
    public ResponseEntity<Item> completeItem(@PathVariable String id) {
        Item item = itemService.findOneItem(id);
        if (item != null) {
            itemService.completeItem(id);
            return ResponseEntity.ok(item);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Mark one item as deleted by item it
    @DeleteMapping("/item/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable String id) {
        Item item = itemService.findOneItem(id);
        if (item != null) {
            itemService.deleteItem(id);
            return ResponseEntity.ok(item);
        }
        return ResponseEntity.notFound().build();
    }
}
