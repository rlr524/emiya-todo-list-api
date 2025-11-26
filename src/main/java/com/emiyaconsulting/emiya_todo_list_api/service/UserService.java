package com.emiyaconsulting.emiya_todo_list_api.service;

import com.emiyaconsulting.emiya_todo_list_api.model.User;
import com.emiyaconsulting.emiya_todo_list_api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public Iterable<User> getUsers() {
        List<User> users = userRepository.findAll();
        List<User> returnedUsers = new ArrayList<>();
        
        for (User user : users) {
            if (!user.isDeleted()) {
                returnedUsers.add(user);
            }
        }
        return returnedUsers;
    }
    
    public User findOneUser(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }
    
    public User updateUser(String id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(updatedUser.getFirstName() != null 
                    ? updatedUser.getFirstName() 
                    : optionalUser.get().getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setUserName(updatedUser.getUserName());
            
            return userRepository.save(existingUser);
        }
        log.info("Could not update user - user with id {} not found", id);
        return null;
    }
    
    public User deleteUser(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setDeleted(true);
            existingUser.setActive(false);
            existingUser.setDeletedAt(Instant.now());
            
            return userRepository.save(existingUser);
        }
        log.info("Could not flag user as deleted - user with id {} not found", id);
        return null;
    }
    
    public User deactivateUser(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setActive(false);
            
            return userRepository.save(existingUser);
        }
        log.info("Could not set user as inactive - user with id {} not found", id);
        return null;
    }
}