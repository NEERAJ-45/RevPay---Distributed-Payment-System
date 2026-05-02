package com.neeraj.upi.wallet.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class WalletResponse {
    private UUID id;
    private UUID userId;
    private String upiId;
    private BigDecimal balance;
    private Instant createdAt;
}
