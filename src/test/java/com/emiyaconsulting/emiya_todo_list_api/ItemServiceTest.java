package com.emiyaconsulting.emiya_todo_list_api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.emiyaconsulting.emiya_todo_list_api.model.Item;
import com.emiyaconsulting.emiya_todo_list_api.repository.ItemRepository;
import com.emiyaconsulting.emiya_todo_list_api.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void testFindOneItemFound() {
        Item item = new Item();
        item.setId("123");
        when(itemRepository.findById("123")).thenReturn(Optional.of(item));

        Item result = itemService.findOneItem("123");
        assertNotNull(result);
        assertEquals("123", result.getId());
    }

    @Test
    void testFindOneItemNotFound() {
        Item result = itemService.findOneItem("456");
        assertNull(result);
    }
}

