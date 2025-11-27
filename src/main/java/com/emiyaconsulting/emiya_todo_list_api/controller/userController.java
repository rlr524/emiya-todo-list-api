package com.emiyaconsulting.emiya_todo_list_api.controller;

import com.emiyaconsulting.emiya_todo_list_api.model.User;
import com.emiyaconsulting.emiya_todo_list_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class userController {
    private final UserService userService;

    public userController(UserService userService) {
        this.userService = userService;
    }
    
    // Insert a single user
    @PostMapping("/user")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
    
    // Return all users
    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return userService.getUsers();
    }
    
    // Return one user by id
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable String id) {
        return userService.findOneUser(id);
    }

    // Update one user by id
    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    // Flag one user as deleted by id
    @DeleteMapping("/user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id) {
        User deletedUser = userService.deleteUser(id);
        if (deletedUser != null) {
            return ResponseEntity.ok(deletedUser);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Flag one user as inactive by id
    @PutMapping("/user/deactivate/{id}")
    public ResponseEntity<User> deactivateUser(@PathVariable String id) {
        User deactivatedUser = userService.deactivateUser(id);
        if (deactivatedUser != null) {
            return ResponseEntity.ok(deactivatedUser);
        }
        return ResponseEntity.notFound().build();
    }
}
