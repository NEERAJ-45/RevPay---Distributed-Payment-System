package com.neeraj.upi.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Core user entity — owns the upi_users DB schema.
 * PIN is always stored as a BCrypt hash, never in plaintext.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @Column(unique = true, length = 100)
    private String email;

    /** Virtual Payment Address — e.g. neeraj@miniupi */
    @Column(name = "upi_id", nullable = false, unique = true, length = 50)
    private String upiId;

    /** BCrypt hashed PIN — never store raw */
    @Column(name = "pin_hash", nullable = false)
    private String pinHash;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
