package com.emiyaconsulting.emiya_todo_list_api.controller;

import com.emiyaconsulting.emiya_todo_list_api.model.Item;
import com.emiyaconsulting.emiya_todo_list_api.service.ItemService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;
    
    // Insert a single item
    // The taint analysis warning is suppressed because we are sanitizing the input
    // using StringEscapeUtils.escapeHtml4 to prevent XSS attacks.
    @SuppressWarnings("JvmTaintAnalysis")
    @PostMapping("/item")
    public ResponseEntity<Item> saveItem(@RequestBody Item item) {
        if (item != null) {
            if (item.getDescription() != null) {
                item.setDescription(StringEscapeUtils.escapeHtml4(item.getDescription()));
            }
            if (item.getTitle() != null) {
                item.setTitle(StringEscapeUtils.escapeHtml4(item.getTitle()));
            }
            if (item.getOwner() != null) {
                item.setOwner(StringEscapeUtils.escapeHtml4(item.getOwner()));
            }
        }

        Item savedItem = itemService.saveItem(item);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }
    
    @GetMapping("/items")
    public Iterable<Item> getItems() {
        return itemService.getItems();
    }
}
