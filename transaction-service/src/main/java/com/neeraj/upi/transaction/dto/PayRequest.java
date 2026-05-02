package com.neeraj.upi.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayRequest {

    /**
     * Client-generated UUID — used as idempotency key.
     * If same requestId is sent twice, the second call returns the first result.
     */
    @NotBlank(message = "requestId is required for idempotency")
    @Size(max = 100)
    private String requestId;

    @NotBlank(message = "Receiver UPI ID is required")
    private String toUpiId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Minimum payment amount is ₹1")
    private BigDecimal amount;

    private String note; // Optional payment description
}
