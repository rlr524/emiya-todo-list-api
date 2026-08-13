package com.emiyaconsulting.todo_list_api.service;

import com.emiyaconsulting.todo_list_api.exception.UserNotFoundException;
import com.emiyaconsulting.todo_list_api.model.User;
import com.emiyaconsulting.todo_list_api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        savedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.info("Created user with id {}", savedUser.getId());
        return savedUser;
    }
    
    public Iterable<User> getUsers() {
        List<User> users = userRepository.findAll();
        List<User> returnedUsers = new ArrayList<>();
        
        for (User user : users) {
            if (!user.isDeleted()) {
                returnedUsers.add(user);
            }
        }
        logger.debug("Returning {} non-deleted users out of {} total", 
                returnedUsers.size(), users.size());
        return returnedUsers;
    }
    
    public User findOneUser(String id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("No user with the id %s is available", id)));
    }
    
    public User updateUser(String id, User updatedUser) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(updatedUser.getFirstName() != null 
                    ? updatedUser.getFirstName() 
                    : optionalUser.get().getFirstName());
            existingUser.setLastName(updatedUser.getLastName() != null 
                    ? updatedUser.getLastName() 
                    : optionalUser.get().getLastName());
            existingUser.setUserName(updatedUser.getUserName() != null 
                    ? updatedUser.getUserName() 
                    : optionalUser.get().getUserName());
            existingUser.setEmail(updatedUser.getEmail() != null 
                    ? updatedUser.getEmail() 
                    : optionalUser.get().getEmail());
            
            User saved = userRepository.save(existingUser);
            logger.info("Updated user {}", id);
            return saved;
        }
        throw new UserNotFoundException(String.format("No user with id %s is available", id));
    }
    
    public User deleteUser(String id) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setDeleted(true);
            existingUser.setActive(false);
            existingUser.setDeletedAt(Instant.now());
            
            User saved = userRepository.save(existingUser);
            logger.info("Set deleted flag to true user {}", id);
            return saved;
        }
        throw new UserNotFoundException(String.format("No user with the id %s is available", id));
    }
    
    public User deactivateUser(String id) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setActive(false);
            
            User saved = userRepository.save(existingUser);
            logger.info("Deactivated user {}", id);
            return saved;
        }
        throw new UserNotFoundException(String.format("No user with the id %s is available", id));
    }
}