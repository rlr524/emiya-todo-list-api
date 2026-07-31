package com.emiyaconsulting.todo_list_api.service;

import com.emiyaconsulting.todo_list_api.exception.UserNotFoundException;
import com.emiyaconsulting.todo_list_api.model.User;
import com.emiyaconsulting.todo_list_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Unit tests for UserService's business logic
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // Creating a user should encode the raw password before saving
    @Test
    void createUser_encodesPasswordAndSaves() {
        User user = new User();
        user.setUserName("someuser");
        user.setEmail("someuser@example.com");
        user.setPassword("plaintext");

        when(passwordEncoder.encode("plaintext")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.createUser(user);

        assertEquals("encoded-password", result.getPassword());
        verify(passwordEncoder).encode("plaintext");
        verify(userRepository).save(user);
    }

    // The user list should filter out soft-deleted users
    @Test
    void getUsers_excludesDeletedUsers() {
        User activeUser = new User();
        activeUser.setUserName("active");
        activeUser.setDeleted(false);

        User deletedUser = new User();
        deletedUser.setUserName("deleted");
        deletedUser.setDeleted(true);

        when(userRepository.findAll()).thenReturn(List.of(activeUser, deletedUser));

        Iterable<User> result = userService.getUsers();

        List<User> resultList = new java.util.ArrayList<>();
        result.forEach(resultList::add);
        assertEquals(1, resultList.size());
        assertEquals("active", resultList.getFirst().getUserName());
    }

    // Fetching a known user id should return that user
    @Test
    void findOneUser_userExists_returnsUser() throws UserNotFoundException {
        User existingUser = new User();
        existingUser.setUserName("someuser");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(existingUser));

        User result = userService.findOneUser("user-1");

        assertEquals("someuser", result.getUserName());
    }

    // Fetching an unknown user id should throw instead of returning null
    @Test
    void findOneUser_userNotFound_throwsUserNotFoundException() {
        when(userRepository.findById("missing-id")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findOneUser("missing-id"));
    }

    // Supplying every field should overwrite every corresponding field on the existing user
    @Test
    void updateUser_allFieldsProvided_updatesAllFields() throws UserNotFoundException {
        User existingUser = new User();
        existingUser.setFirstName("First");
        existingUser.setLastName("Last");
        existingUser.setUserName("someuser");
        existingUser.setEmail("someuser@example.com");

        User updatedUser = new User();
        updatedUser.setFirstName("NewFirst");
        updatedUser.setLastName("NewLast");
        updatedUser.setUserName("newuser");
        updatedUser.setEmail("newuser@example.com");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser("user-1", updatedUser);

        assertEquals("NewFirst", result.getFirstName());
        assertEquals("NewLast", result.getLastName());
        assertEquals("newuser", result.getUserName());
        assertEquals("newuser@example.com", result.getEmail());
    }

    // Leaving fields null on the update should keep the existing user's values for those fields
    @Test
    void updateUser_partialFieldsProvided_retainsExistingValues() throws UserNotFoundException {
        User existingUser = new User();
        existingUser.setFirstName("First");
        existingUser.setLastName("Last");
        existingUser.setUserName("someuser");
        existingUser.setEmail("someuser@example.com");

        User updatedUser = new User();
        updatedUser.setFirstName("NewFirst");
        // lastName, userName, and email intentionally left null

        when(userRepository.findById("user-1")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser("user-1", updatedUser);

        assertEquals("NewFirst", result.getFirstName());
        assertEquals("Last", result.getLastName());
        assertEquals("someuser", result.getUserName());
        assertEquals("someuser@example.com", result.getEmail());
    }

    // Updating a missing user should throw instead of creating one
    @Test
    void updateUser_userNotFound_throwsUserNotFoundException() {
        when(userRepository.findById("missing-id")).thenReturn(Optional.empty());

        User updatedUser = new User();
        updatedUser.setFirstName("Anything");

        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser("missing-id", updatedUser));
    }

    // Deleting a user should soft-delete it: mark it deleted, inactive, and timestamp it
    @Test
    void deleteUser_userExists_marksUserDeletedAndInactive() throws UserNotFoundException {
        User existingUser = new User();
        existingUser.setActive(true);
        existingUser.setDeleted(false);

        when(userRepository.findById("user-1")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.deleteUser("user-1");

        assertTrue(result.isDeleted());
        assertFalse(result.isActive());
        assertNotNull(result.getDeletedAt());
    }

    // Deleting a missing user should throw instead of silently doing nothing
    @Test
    void deleteUser_userNotFound_throwsUserNotFoundException() {
        when(userRepository.findById("missing-id")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser("missing-id"));
    }

    // Deactivating a user should mark it inactive without deleting it
    @Test
    void deactivateUser_userExists_marksUserInactive() throws UserNotFoundException {
        User existingUser = new User();
        existingUser.setActive(true);

        when(userRepository.findById("user-1")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.deactivateUser("user-1");

        assertFalse(result.isActive());
    }

    // Deactivating a missing user should throw instead of silently doing nothing
    @Test
    void deactivateUser_userNotFound_throwsUserNotFoundException() {
        when(userRepository.findById("missing-id")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.deactivateUser("missing-id"));
    }
}
