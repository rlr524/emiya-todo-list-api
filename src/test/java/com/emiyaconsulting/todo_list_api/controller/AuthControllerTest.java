package com.emiyaconsulting.todo_list_api.controller;

import com.emiyaconsulting.todo_list_api.dto.LoginRequest;
import com.emiyaconsulting.todo_list_api.model.User;
import com.emiyaconsulting.todo_list_api.security.JwtUtil;
import com.emiyaconsulting.todo_list_api.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Unit tests for AuthController's login and register endpoints
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    // Correct credentials should return a JWT in the response body
    @Test
    void userLogin_validCredentials_returnsOkWithToken() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("someuser");
        loginRequest.setPassword("password");

        when(jwtUtil.generateToken("someuser")).thenReturn("token123");

        ResponseEntity<String> response = authController.userLogin(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token123", response.getBody());
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("someuser", "password"));
    }

    // Wrong credentials should fail authentication instead of issuing a token
    @Test
    void userLogin_invalidCredentials_throwsBadCredentialsException() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("someuser");
        loginRequest.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authController.userLogin(loginRequest));
        verifyNoInteractions(jwtUtil);
    }

    // A new username should register successfully and return the saved user
    @Test
    void register_newUser_returnsOkWithCreatedUser() {
        User user = new User();
        user.setUserName("someuser");

        User savedUser = new User();
        savedUser.setId("user-1");
        savedUser.setUserName("someuser");

        when(userService.createUser(user)).thenReturn(savedUser);

        ResponseEntity<User> response = authController.register(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedUser, response.getBody());
    }

    // Registering an already-taken username should fail instead of overwriting the existing user
    @Test
    void register_duplicateUserName_throwsDuplicateKeyException() {
        User user = new User();
        user.setUserName("someuser");

        when(userService.createUser(user)).thenThrow(new DuplicateKeyException("Duplicate username"));

        assertThrows(DuplicateKeyException.class, () -> authController.register(user));
    }
}
