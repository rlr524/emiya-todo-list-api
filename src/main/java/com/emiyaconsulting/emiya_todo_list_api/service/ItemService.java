package com.emiyaconsulting.emiya_todo_list_api.service;

import com.emiyaconsulting.emiya_todo_list_api.model.Item;
import com.emiyaconsulting.emiya_todo_list_api.repository.ItemRepository;
import org.springframework.stereotype.Service;

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
}
