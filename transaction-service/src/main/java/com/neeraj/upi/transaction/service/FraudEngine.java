package com.neeraj.upi.transaction.service;

import com.neeraj.upi.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * Velocity and fraud rule checks.
 *
 * Rules enforced:
 *  1. Daily limit  — total outgoing amount per user per calendar day <= ₹10,000 (configurable)
 *  2. Per-txn cap  — single payment amount <= ₹50,000 (configurable)
 *  3. Self-pay     — sender and receiver UPI IDs must be different
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FraudEngine {

    private final TransactionRepository transactionRepository;

    @Value("${fraud.daily-limit:10000.00}")
    private BigDecimal dailyLimit;

    @Value("${fraud.max-per-txn:50000.00}")
    private BigDecimal maxPerTxn;

    /**
     * Runs all fraud checks. Throws FraudVelocityException on failure.
     *
     * @param senderUpiId  who is paying
     * @param receiverUpiId who receives
     * @param amount        payment amount
     */
    public void validate(String senderUpiId, String receiverUpiId, BigDecimal amount) {
        // TODO:
        // 1. Check amount <= maxPerTxn, else throw FraudVelocityException("AMOUNT_EXCEEDS_LIMIT")
        // 2. Check senderUpiId != receiverUpiId, else throw FraudVelocityException("SELF_PAYMENT")
        // 3. Compute startOfDay = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant()
        // 4. BigDecimal dailySent = transactionRepository.sumSuccessfulAmountSince(senderUpiId, startOfDay)
        // 5. Check dailySent + amount <= dailyLimit, else throw FraudVelocityException("DAILY_LIMIT_EXCEEDED")
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
