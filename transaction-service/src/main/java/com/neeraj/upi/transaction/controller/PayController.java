package com.neeraj.upi.transaction.controller;

import com.neeraj.upi.common.dto.ApiResponse;
import com.neeraj.upi.transaction.dto.PayRequest;
import com.neeraj.upi.transaction.dto.PayResponse;
import com.neeraj.upi.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "P2P payments, status check, history")
@SecurityRequirement(name = "bearerAuth")
public class PayController {

    private final TransactionService transactionService;

    /**
     * Initiate a payment.
     * Sender is extracted from the JWT — NOT from the request body.
     */
    @PostMapping("/pay")
    @Operation(summary = "Send money to a UPI ID")
    public ResponseEntity<ApiResponse<PayResponse>> pay(
            @Valid @RequestBody PayRequest request,
            @RequestHeader("Authorization") String authHeader) {
        // TODO:
        // 1. Extract senderUpiId from JWT (use JwtService)
        // 2. Call transactionService.pay(request, senderUpiId)
        // 3. Return 201 if new transaction, 200 if replayed (idempotency)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping("/{txnId}")
    @Operation(summary = "Get transaction status by ID")
    public ResponseEntity<ApiResponse<PayResponse>> getById(@PathVariable UUID txnId) {
        // TODO: return transactionService.getById(txnId)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping("/history/{upiId}")
    @Operation(summary = "Get paginated transaction history for a UPI ID")
    public ResponseEntity<ApiResponse<?>> getHistory(
            @PathVariable String upiId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: return transactionService.getHistory(upiId, page, size)
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
