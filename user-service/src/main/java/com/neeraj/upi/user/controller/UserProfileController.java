package com.neeraj.upi.user.controller;

import com.neeraj.upi.common.dto.ApiResponse;
import com.neeraj.upi.user.dto.UserProfileResponse;
import com.neeraj.upi.user.service.JwtService;
import com.neeraj.upi.user.service.QrCodeService;
import com.neeraj.upi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Profile lookup and QR code generation")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {

    private final UserService   userService;
    private final JwtService    jwtService;
    private final QrCodeService qrCodeService;

    @GetMapping("/me")
    @Operation(summary = "Get current logged-in user's profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(
            @RequestHeader("Authorization") String authHeader) {
        // TODO: extract userId from JWT, return userService.getByUserId(userId)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping("/{upiId}")
    @Operation(summary = "Lookup any user by their UPI ID (for payment resolution)")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getByUpiId(
            @PathVariable String upiId) {
        // TODO: return userService.getByUpiId(upiId)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping("/qr/{upiId}")
    @Operation(summary = "Get QR code for a UPI ID (returns Base64 PNG)")
    public ResponseEntity<ApiResponse<Map<String, String>>> getQrCode(
            @PathVariable String upiId) {
        // TODO: get profile, generate QR, return { upiId, qrBase64, upiUri }
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
