package com.travis.blog.services.impl;

import com.travis.blog.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    private long jwtExpiryMs = 86400000L; // 1 day in milliseconds

    @Override
    public UserDetails authenticate(String email, String password) {
        // Authenticate user via Spring Security's manager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // If successful, return UserDetails
        return userDetailsService.loadUserByUsername(email);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims) // Extra payload data (optional)
                .setSubject(userDetails.getUsername()) // Typically email or username
                .setIssuedAt(new Date()) // Token creation timestamp
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryMs)) // Expiry time
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Sign with secret key
                .compact();
    }

    @Override
    public UserDetails validateToken(String token) {
        // Extract username from token and load user
        String username = extractUsername(token);
        return userDetailsService.loadUserByUsername(username);
    }

    private String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // subject contains username/email
    }

    private Key getSignInKey() {
        // Convert secret string to signing key
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
