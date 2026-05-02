package com.neeraj.upi.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/** Mirrors UserCreatedEvent from user-service — deserialized from Kafka */
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
