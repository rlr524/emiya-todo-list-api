package com.emiyaconsulting.emiya_todo_list_api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.emiyaconsulting.emiya_todo_list_api.model.Item;
import com.emiyaconsulting.emiya_todo_list_api.repository.ItemRepository;
import com.emiyaconsulting.emiya_todo_list_api.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

@SpringBootTest
class ItemServiceTest {

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;
    
    @TestConfiguration
    static class TestConfig {
        @Bean
        public ItemService itemService(ItemRepository itemRepository) {
            return new ItemService(itemRepository);
        }
    }
    
    @Test
    void testFindOneItemFound() {
        Item item = new Item();
        item.setId("456");
        when(itemRepository.findById("456")).thenReturn(Optional.of(item));
        
        Item result = itemService.findOneItem("456");
        assertNotNull(result);
        assertEquals("456", result.getId());
    }

    @Test
    void testFindOneItemNotFound() {
        when(itemRepository.findById("456")).thenReturn(Optional.empty());
        
        Item result = itemService.findOneItem("456");
        assertNull(result);
    }
}

