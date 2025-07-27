package com.travis.blog.controllers;

import com.travis.blog.domain.dto.AuthResponse;
import com.travis.blog.domain.dto.LoginRequest;
import com.travis.blog.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth/login")
@RequiredArgsConstructor // generates a constructor for the final field
public class AuthController {

    private final AuthenticationService authenticationService;

    // Endpoint to handle login requests
    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        // Validate the user credentials
        UserDetails userDetails = authenticationService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        // Generate a JWT token for the authenticated user
        String tokenValue = authenticationService.generateToken(userDetails);

        // Construct the response with the token and expiry (24 hours = 86400 secs)
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();

        // Send the token back to the client
        return ResponseEntity.ok(authResponse);
    }
}
