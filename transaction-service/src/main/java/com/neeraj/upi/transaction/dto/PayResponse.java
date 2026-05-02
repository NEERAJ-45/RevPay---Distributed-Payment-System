package com.neeraj.upi.transaction.dto;

import com.neeraj.upi.transaction.entity.Transaction.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class PayResponse {
    private UUID   txnId;
    private String requestId;
    private String senderUpiId;
    private String receiverUpiId;
    private BigDecimal amount;
    private String note;
    private TransactionStatus status;
    private String failureReason;   // null on success
    private Instant createdAt;
    private boolean replayed;       // true if this was a duplicate requestId response
}
