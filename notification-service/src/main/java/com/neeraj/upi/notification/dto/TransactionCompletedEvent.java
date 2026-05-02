package com.neeraj.upi.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** Mirrors TransactionCompletedEvent from transaction-service — deserialized from Kafka */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCompletedEvent {
    private UUID txnId;
    private String requestId;
    private String senderUpiId;
    private String receiverUpiId;
    private BigDecimal amount;
    private String status;          // "SUCCESS" or "FAILED"
    private String failureReason;
    private Instant completedAt;
}
