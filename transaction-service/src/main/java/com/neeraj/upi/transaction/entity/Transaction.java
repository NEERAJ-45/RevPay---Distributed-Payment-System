package com.neeraj.upi.transaction.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Records every payment attempt.
 * Lifecycle: PENDING → SUCCESS or PENDING → FAILED
 *
 * request_id = client-provided idempotency key (unique constraint).
 * If same request_id comes in twice, the second call returns the first result.
 */
@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Client-generated idempotency key — UNIQUE in DB */
    @Column(name = "request_id", nullable = false, unique = true, length = 100)
    private String requestId;

    @Column(name = "sender_upi_id", nullable = false, length = 50)
    private String senderUpiId;

    @Column(name = "receiver_upi_id", nullable = false, length = 50)
    private String receiverUpiId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(length = 255)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(name = "failure_reason", length = 255)
    private String failureReason;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public enum TransactionStatus {
        PENDING, SUCCESS, FAILED
    }
}
