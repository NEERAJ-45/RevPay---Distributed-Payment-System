package com.neeraj.upi.wallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Internal-only request — called by transaction-service via OpenFeign.
 * NOT exposed via API Gateway.
 */
@Data
public class TransferRequest {

    @NotNull
    private UUID transactionId;     // cross-service correlation ID

    @NotBlank
    private String fromUpiId;

    @NotBlank
    private String toUpiId;

    @NotNull
    @DecimalMin(value = "1.00")
    private BigDecimal amount;

    private String note;
}
