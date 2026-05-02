package com.neeraj.upi.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String upiId;
    private String fullName;
    private String tokenType;

    public static AuthResponse of(String token, String upiId, String fullName) {
        return AuthResponse.builder()
                .token(token)
                .upiId(upiId)
                .fullName(fullName)
                .tokenType("Bearer")
                .build();
    }
}
