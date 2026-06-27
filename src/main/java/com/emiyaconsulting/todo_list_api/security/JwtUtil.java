package com.emiyaconsulting.todo_list_api.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;

@Component
public class JwtUtil {
    // @Value tells Java look up a key in application.properties named "jwt.secret"
    @Value("${jwt.secret}")
    private String secretKey;
    
    public String generateToken(String userName) {
        Instant now = Instant.now();
        
        return Jwts.builder()
                .subject(userName)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(86400)))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
    
    public String extractUserName(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    
    public boolean validateToken (String token) {
        try {
            extractUserName(token);
        } catch (JwtException e) {
            System.out.printf("Error %s", e);
            return false;
        }
        return true;
    }
    
}
