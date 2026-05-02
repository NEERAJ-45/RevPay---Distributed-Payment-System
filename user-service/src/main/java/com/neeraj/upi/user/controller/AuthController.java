package com.neeraj.upi.user.controller;

import com.neeraj.upi.common.dto.ApiResponse;
import com.neeraj.upi.user.dto.AuthResponse;
import com.neeraj.upi.user.dto.LoginRequest;
import com.neeraj.upi.user.dto.RegisterRequest;
import com.neeraj.upi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and login")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user — returns JWT + UPI ID")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        // TODO: return 201 Created with userService.register(request)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PostMapping("/login")
    @Operation(summary = "Login with phone + PIN — returns JWT")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        // TODO: return 200 OK with userService.login(request)
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
