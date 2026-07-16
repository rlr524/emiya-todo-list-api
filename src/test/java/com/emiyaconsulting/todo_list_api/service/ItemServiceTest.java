package com.emiyaconsulting.todo_list_api.service;

import com.emiyaconsulting.todo_list_api.exception.ItemNotFoundException;
import com.emiyaconsulting.todo_list_api.repository.ItemRepository;
import com.emiyaconsulting.todo_list_api.model.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private ItemService itemService;

    @Test
    void updateItem_allFieldsProvided_updateAllFields() {
        // Arrange: build an "existingItem" (what's currently in the DB)
        Item existingItem = new Item();
        existingItem.setTitle("Old Title");
        existingItem.setItemDescription("Old description");
        existingItem.setImportance("LOW");
        existingItem.setDue(LocalDate.of(2026, 1, 1));
        existingItem.setOwner("user-1");
        existingItem.setComplete(false);

        // and an "updatedItem" (the incoming change with all fields set)
        Item updatedItem = new Item();
        updatedItem.setTitle("New Title");
        updatedItem.setItemDescription("New description");
        updatedItem.setImportance("HIGH");
        updatedItem.setDue(LocalDate.of(2026, 12, 31));
        updatedItem.setOwner("user-2");
        updatedItem.setComplete(true);

        // Stub: return the existing item for the lookup, and echo back whatever is saved
        when(itemRepository.findById("item-1")).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(any(Item.class))).thenAnswer(i -> i.getArgument(0));

        // Act: call itemService.updateItem(id, updatedItem)
        Item result = itemService.updateItem("item-1", updatedItem);

        // Assert: check the returned Item's fields match updatedItem's values
        assertEquals("New Title", result.getTitle());
        assertEquals("New description", result.getItemDescription());
        assertEquals("HIGH", result.getImportance());
        assertEquals(LocalDate.of(2026, 12, 31), result.getDue());
        assertEquals("user-2", result.getOwner());
        assertTrue(result.isComplete());
    }

    @Test
    void updateItem_itemNotFound_throwsItemNotFoundException() {
        // Arrange: no item exists for the given id
        when(itemRepository.findById("missing-id")).thenReturn(Optional.empty());

        Item updatedItem = new Item();
        updatedItem.setTitle("Anything");

        // Act & Assert: service must throw ItemNotFoundException
        assertThrows(ItemNotFoundException.class,
                () -> itemService.updateItem("missing-id", updatedItem));
    }
}