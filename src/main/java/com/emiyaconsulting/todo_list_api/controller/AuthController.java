package com.emiyaconsulting.todo_list_api.controller;

import com.emiyaconsulting.todo_list_api.dto.LoginRequest;
import com.emiyaconsulting.todo_list_api.security.JwtUtil;
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

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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
}
