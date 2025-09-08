package com.emiyaconsulting.emiya_todo_list_api.controller;

import com.emiyaconsulting.emiya_todo_list_api.model.Item;
import com.emiyaconsulting.emiya_todo_list_api.service.ItemService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    
    @GetMapping("/items")
    public Iterable<Item> getItems() {
        return itemService.getItems();
    }
    
    @GetMapping("/")
    public String healthCheck() {
        return "OK";
    }
}
