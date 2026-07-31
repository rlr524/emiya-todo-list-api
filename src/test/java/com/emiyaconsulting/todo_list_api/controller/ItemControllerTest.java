package com.emiyaconsulting.todo_list_api.controller;

import com.emiyaconsulting.todo_list_api.exception.ItemNotFoundException;
import com.emiyaconsulting.todo_list_api.model.Item;
import com.emiyaconsulting.todo_list_api.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemService itemService;

    @Mock
    private Principal principal;

    @InjectMocks
    private ItemController itemController;

    @Test
    void createItem_returnsOkWithCreatedItem() {
        Item item = new Item();
        item.setTitle("Buy milk");

        Item savedItem = new Item();
        savedItem.setId("item-1");
        savedItem.setTitle("Buy milk");
        savedItem.setOwner("someuser");

        when(principal.getName()).thenReturn("someuser");
        when(itemService.createItem(item)).thenReturn(savedItem);

        ResponseEntity<Item> response = itemController.createItem(item, principal);

        assertEquals("someuser", item.getOwner());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedItem, response.getBody());
        verify(itemService).createItem(item);
    }

    @Test
    void getItems_returnsAllItems() {
        Item item = new Item();
        item.setTitle("Buy milk");

        when(itemService.getItems()).thenReturn(List.of(item));

        Iterable<Item> result = itemController.getItems();

        assertEquals(List.of(item), result);
    }

    @Test
    void getItemsByUser_returnsItemsForUser() {
        Item item = new Item();
        item.setTitle("Buy milk");
        item.setOwner("user-1");

        when(itemService.getItemsByUser("user-1")).thenReturn(List.of(item));

        Iterable<Item> result = itemController.getItemsByUser("user-1");

        assertEquals(List.of(item), result);
    }

    @Test
    void getMyItems_returnsOkWithItemsForLoggedInUser() {
        Item item = new Item();
        item.setTitle("Buy milk");
        item.setOwner("someuser");

        when(principal.getName()).thenReturn("someuser");
        when(itemService.getItemsByUser("someuser")).thenReturn(List.of(item));

        ResponseEntity<Iterable<Item>> response = itemController.getMyItems(principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(item), response.getBody());
    }

    @Test
    void getItem_itemExists_returnsItem() {
        Item item = new Item();
        item.setTitle("Buy milk");

        when(itemService.findOneItem("item-1")).thenReturn(item);

        Item result = itemController.getItem("item-1");

        assertEquals(item, result);
    }

    @Test
    void getItem_itemNotFound_throwsItemNotFoundException() {
        when(itemService.findOneItem("missing-id"))
                .thenThrow(new ItemNotFoundException("No item with the id missing-id is available"));

        assertThrows(ItemNotFoundException.class, () -> itemController.getItem("missing-id"));
    }

    @Test
    void updatedItem_itemExists_returnsOkWithUpdatedItem() {
        Item itemDetails = new Item();
        itemDetails.setTitle("New title");

        Item updatedItem = new Item();
        updatedItem.setTitle("New title");

        when(itemService.updateItem("item-1", itemDetails)).thenReturn(updatedItem);

        ResponseEntity<Item> response = itemController.updatedItem("item-1", itemDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedItem, response.getBody());
    }

    @Test
    void updatedItem_itemNotFound_returnsNotFound() {
        Item itemDetails = new Item();
        itemDetails.setTitle("New title");

        when(itemService.updateItem("missing-id", itemDetails)).thenReturn(null);

        ResponseEntity<Item> response = itemController.updatedItem("missing-id", itemDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void completeItem_itemExists_returnsOkWithCompletedItem() {
        Item item = new Item();
        item.setTitle("Buy milk");

        when(itemService.findOneItem("item-1")).thenReturn(item);

        ResponseEntity<Item> response = itemController.completeItem("item-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item, response.getBody());
        verify(itemService).completeItem("item-1");
    }

    @Test
    void completeItem_itemNotFound_returnsNotFound() {
        when(itemService.findOneItem("missing-id")).thenReturn(null);

        ResponseEntity<Item> response = itemController.completeItem("missing-id");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(itemService, never()).completeItem(anyString());
    }

    @Test
    void deleteItem_itemExists_returnsOkWithDeletedItem() {
        Item item = new Item();
        item.setTitle("Buy milk");

        when(itemService.findOneItem("item-1")).thenReturn(item);

        ResponseEntity<Item> response = itemController.deleteItem("item-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item, response.getBody());
        verify(itemService).deleteItem("item-1");
    }

    @Test
    void deleteItem_itemNotFound_returnsNotFound() {
        when(itemService.findOneItem("missing-id")).thenReturn(null);

        ResponseEntity<Item> response = itemController.deleteItem("missing-id");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(itemService, never()).deleteItem(anyString());
    }
}
