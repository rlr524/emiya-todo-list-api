package com.emiyaconsulting.todo_list_api.controller;

import com.emiyaconsulting.todo_list_api.dto.LoginRequest;
import com.emiyaconsulting.todo_list_api.model.User;
import com.emiyaconsulting.todo_list_api.security.JwtUtil;
import com.emiyaconsulting.todo_list_api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, 
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }
    
    @PostMapping("/auth/login")
    public ResponseEntity<String> userLogin(@RequestBody LoginRequest loginRequest) {
        String userName = loginRequest.getUserName();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userName,
                        loginRequest.getPassword()
                )
        );
        
        return ResponseEntity.ok(jwtUtil.generateToken(userName));
    }
    
    @PostMapping("/auth/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
}
