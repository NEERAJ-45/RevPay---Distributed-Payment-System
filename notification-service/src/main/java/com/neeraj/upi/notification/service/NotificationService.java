package com.neeraj.upi.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Mock notification sender — in production this would call Twilio, Firebase, or SES.
 * For now, all alerts are printed to console log only.
 */
@Service
@Slf4j
public class NotificationService {

    /** Sends a welcome message after user registration */
    public void sendWelcome(String upiId, String fullName, String phone) {
        log.info("[SMS → {}] Welcome {}! Your UPI ID is {}. Start sending money!", phone, fullName, upiId);
        // Future: integrate real SMS provider (Twilio, AWS SNS, MSG91)
    }

    /** Sends a debit alert to the payment sender */
    public void sendDebitAlert(String phone, String upiId, BigDecimal amount, String txnId) {
        log.info("[SMS → {}] ₹{} debited from {}. Txn Ref: {}. -MiniUPI", phone, amount, upiId, txnId);
    }

    /** Sends a credit alert to the payment receiver */
    public void sendCreditAlert(String phone, String upiId, BigDecimal amount, String txnId) {
        log.info("[SMS → {}] ₹{} credited to {}. Txn Ref: {}. -MiniUPI", phone, amount, upiId, txnId);
    }

    /** Sends a payment failed alert to the sender */
    public void sendFailureAlert(String phone, BigDecimal amount, String reason) {
        log.warn("[SMS → {}] Payment of ₹{} failed. Reason: {}. -MiniUPI", phone, amount, reason);
    }
}
