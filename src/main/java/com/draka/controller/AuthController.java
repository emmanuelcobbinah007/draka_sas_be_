package com.draka.controller;

import com.draka.dto.ApiResponse;
import com.draka.dto.JwtResponse;
import com.draka.dto.LoginRequest;
import com.draka.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication endpoints.
 * Public endpoints for login.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * Login endpoint.
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and get JWT token")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Test endpoint to verify authentication.
     */
    @GetMapping("/test")
    @Operation(summary = "Test authentication", description = "Test if authentication is working")
    public ResponseEntity<ApiResponse> test() {
        return ResponseEntity.ok(new ApiResponse(true, "Authentication is working"));
    }
}
