package com.emiyaconsulting.todo_list_api.controller;

import com.emiyaconsulting.todo_list_api.exception.UserNotFoundException;
import com.emiyaconsulting.todo_list_api.model.User;
import com.emiyaconsulting.todo_list_api.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void createUser_returnsOkWithCreatedUser() {
        User user = new User();
        user.setUserName("someuser");

        User savedUser = new User();
        savedUser.setId("user-1");
        savedUser.setUserName("someuser");

        when(userService.createUser(user)).thenReturn(savedUser);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedUser, response.getBody());
        verify(userService).createUser(user);
    }

    @Test
    void getUsers_returnsAllUsers() {
        User user = new User();
        user.setUserName("someuser");

        when(userService.getUsers()).thenReturn(List.of(user));

        Iterable<User> result = userController.getUsers();

        assertEquals(List.of(user), result);
    }

    @Test
    void getUser_userExists_returnsUser() {
        User user = new User();
        user.setUserName("someuser");

        when(userService.findOneUser("user-1")).thenReturn(user);

        User result = userController.getUser("user-1");

        assertEquals(user, result);
    }

    @Test
    void getUser_userNotFound_throwsUserNotFoundException() {
        when(userService.findOneUser("missing-id"))
                .thenThrow(new UserNotFoundException("No user with the id missing-id is available"));

        assertThrows(UserNotFoundException.class, () -> userController.getUser("missing-id"));
    }

    @Test
    void updateUser_userExists_returnsOkWithUpdatedUser() {
        User userDetails = new User();
        userDetails.setFirstName("NewFirst");

        User updatedUser = new User();
        updatedUser.setFirstName("NewFirst");

        when(userService.updateUser("user-1", userDetails)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.updateUser("user-1", userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void updateUser_userNotFound_returnsNotFound() {
        User userDetails = new User();
        userDetails.setFirstName("NewFirst");

        when(userService.updateUser("missing-id", userDetails)).thenReturn(null);

        ResponseEntity<User> response = userController.updateUser("missing-id", userDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteUser_userExists_returnsOkWithDeletedUser() {
        User deletedUser = new User();
        deletedUser.setDeleted(true);

        when(userService.deleteUser("user-1")).thenReturn(deletedUser);

        ResponseEntity<User> response = userController.deleteUser("user-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deletedUser, response.getBody());
    }

    @Test
    void deleteUser_userNotFound_returnsNotFound() {
        when(userService.deleteUser("missing-id")).thenReturn(null);

        ResponseEntity<User> response = userController.deleteUser("missing-id");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deactivateUser_userExists_returnsOkWithDeactivatedUser() {
        User deactivatedUser = new User();
        deactivatedUser.setActive(false);

        when(userService.deactivateUser("user-1")).thenReturn(deactivatedUser);

        ResponseEntity<User> response = userController.deactivateUser("user-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deactivatedUser, response.getBody());
    }

    @Test
    void deactivateUser_userNotFound_returnsNotFound() {
        when(userService.deactivateUser("missing-id")).thenReturn(null);

        ResponseEntity<User> response = userController.deactivateUser("missing-id");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}