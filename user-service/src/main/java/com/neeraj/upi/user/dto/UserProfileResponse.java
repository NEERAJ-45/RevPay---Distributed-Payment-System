package com.neeraj.upi.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class UserProfileResponse {
    private UUID id;
    private String fullName;
    private String phone;
    private String email;
    private String upiId;
    private boolean isActive;
    private Instant createdAt;
}
