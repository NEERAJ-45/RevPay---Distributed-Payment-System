package com.neeraj.upi.user.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Kafka event published when a new user is created.
 * Consumed by: wallet-service (create wallet), notification-service (welcome message).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {
    private UUID userId;
    private String upiId;
    private String fullName;
    private String phone;
    private Instant createdAt;
}
