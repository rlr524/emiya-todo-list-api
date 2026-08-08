package com.emiyaconsulting.todo_list_api.controller;

import com.emiyaconsulting.todo_list_api.dto.CreateItemRequest;
import com.emiyaconsulting.todo_list_api.exception.ItemNotFoundException;
import com.emiyaconsulting.todo_list_api.model.Item;
import com.emiyaconsulting.todo_list_api.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Unit tests for ItemController's CRUD and completion endpoints
@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemService itemService;

    @Mock
    private Principal principal;

    @InjectMocks
    private ItemController itemController;

    // Creating an item should stamp the logged-in user as owner and return the saved item
    @Test
    void createItem_returnsOkWithCreatedItem() {
        CreateItemRequest request = new CreateItemRequest(
                "Buy milk", 
                "From the store", 
                LocalDate.of(2026, 8, 12), 
                "High"
        );

        Item savedItem = new Item();
        savedItem.setId("item-1");
        savedItem.setTitle("Buy milk");
        savedItem.setItemDescription("From the store");
        savedItem.setDue(LocalDate.of(2026, 8, 12));
        savedItem.setImportance("High");
        savedItem.setOwner("someuser");
        
        when(principal.getName()).thenReturn("someuser");

        ArgumentCaptor<Item> itemArgumentCaptor = ArgumentCaptor.forClass(Item.class);
        when (itemService.createItem(itemArgumentCaptor.capture())).thenReturn(savedItem);
        
        ResponseEntity<Item> response = itemController.createItem(request, principal);
        
        Item capturedItem = itemArgumentCaptor.getValue();
        assertEquals("Buy milk", capturedItem.getTitle());
        assertEquals("From the store", capturedItem.getItemDescription());
        assertEquals(LocalDate.of(2026, 8, 12), capturedItem.getDue());
        assertEquals("High", capturedItem.getImportance());
        assertEquals("someuser", capturedItem.getOwner());
        assertFalse(capturedItem.isDeleted());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedItem, response.getBody());
    }

    // Listing items should return everything the service provides
    @Test
    void getItems_returnsAllItems() {
        Item item = new Item();
        item.setTitle("Buy milk");

        when(itemService.getItems()).thenReturn(List.of(item));

        Iterable<Item> result = itemController.getItems();

        assertEquals(List.of(item), result);
    }

    // Listing items for a given user id should only return that user's items
    @Test
    void getItemsByUser_returnsItemsForUser() {
        Item item = new Item();
        item.setTitle("Buy milk");
        item.setOwner("user-1");

        when(itemService.getItemsByUser("user-1")).thenReturn(List.of(item));

        Iterable<Item> result = itemController.getItemsByUser("user-1");

        assertEquals(List.of(item), result);
    }

    // "My items" should use the logged-in principal, not an explicit user id
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

    // Fetching a known item id should return that item
    @Test
    void getItem_itemExists_returnsItem() {
        Item item = new Item();
        item.setTitle("Buy milk");

        when(itemService.findOneItem("item-1")).thenReturn(item);

        Item result = itemController.getItem("item-1");

        assertEquals(item, result);
    }

    // Fetching an unknown item id should throw instead of returning null
    @Test
    void getItem_itemNotFound_throwsItemNotFoundException() {
        when(itemService.findOneItem("missing-id"))
                .thenThrow(new ItemNotFoundException("No item with the id missing-id is available"));

        assertThrows(ItemNotFoundException.class, () -> itemController.getItem("missing-id"));
    }

    // Updating an existing item should return the updated item with an OK status
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

    // Updating a missing item should return 404 rather than throwing
    @Test
    void updatedItem_itemNotFound_returnsNotFound() {
        Item itemDetails = new Item();
        itemDetails.setTitle("New title");

        when(itemService.updateItem("missing-id", itemDetails)).thenReturn(null);

        ResponseEntity<Item> response = itemController.updatedItem("missing-id", itemDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Completing an existing item should mark it complete and return it
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

    // Completing a missing item should return 404 and never call the service to complete it
    @Test
    void completeItem_itemNotFound_returnsNotFound() {
        when(itemService.findOneItem("missing-id")).thenReturn(null);

        ResponseEntity<Item> response = itemController.completeItem("missing-id");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(itemService, never()).completeItem(anyString());
    }

    // Deleting an existing item should return the deleted item
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

    // Deleting a missing item should return 404 and never call the service to delete it
    @Test
    void deleteItem_itemNotFound_returnsNotFound() {
        when(itemService.findOneItem("missing-id")).thenReturn(null);

        ResponseEntity<Item> response = itemController.deleteItem("missing-id");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(itemService, never()).deleteItem(anyString());
    }
}
