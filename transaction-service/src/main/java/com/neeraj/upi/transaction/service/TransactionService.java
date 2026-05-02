package com.neeraj.upi.transaction.service;

import com.neeraj.upi.transaction.dto.PayRequest;
import com.neeraj.upi.transaction.dto.PayResponse;
import com.neeraj.upi.transaction.entity.Transaction;
import com.neeraj.upi.transaction.feign.WalletFeignClient;
import com.neeraj.upi.transaction.kafka.TransactionEventPublisher;
import com.neeraj.upi.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Main orchestrator for the payment flow.
 *
 * Execution order (ALWAYS follow this sequence):
 *  1. Idempotency check  — has this requestId been seen before?
 *  2. Save PENDING txn   — record exists before any money moves
 *  3. Fraud check        — velocity + daily limit validation
 *  4. Wallet transfer    — call wallet-service via Feign (with @Retryable)
 *  5. Update to SUCCESS  — mark txn SUCCESS, store idempotency key in Redis
 *  6. Publish Kafka event — notify notification-service async
 *  On any failure → mark txn FAILED + publish failed event
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository  transactionRepository;
    private final IdempotencyService     idempotencyService;
    private final FraudEngine            fraudEngine;
    private final WalletFeignClient      walletFeignClient;
    private final TransactionEventPublisher eventPublisher;

    /**
     * Initiates a P2P payment.
     * The senderUpiId is extracted from the JWT in the controller — never trusted from request body.
     */
    @Transactional
    public PayResponse pay(PayRequest request, String senderUpiId) {
        // TODO: follow the 6-step execution order above
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** Fetches a single transaction by its ID */
    @Transactional(readOnly = true)
    public PayResponse getById(UUID txnId) {
        // TODO: find by ID, map to PayResponse
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** Returns paginated transaction history for a UPI ID (sender or receiver) */
    @Transactional(readOnly = true)
    public Object getHistory(String upiId, int page, int size) {
        // TODO: transactionRepository.findHistoryByUpiId(upiId, PageRequest.of(page, size))
        //       map Page<Transaction> to Page<PayResponse>
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** Maps a Transaction entity to a PayResponse DTO */
    private PayResponse toResponse(Transaction txn, boolean replayed) {
        // TODO: use builder to map all fields
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
