package com.neeraj.upi.notification.kafka;

import com.neeraj.upi.common.constants.KafkaTopics;
import com.neeraj.upi.notification.dto.TransactionCompletedEvent;
import com.neeraj.upi.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Listens to txn.completed and txn.failed events.
 * Sends debit alert to sender AND credit alert to receiver on success.
 * Sends failure alert to sender on failure.
 *
 * Note: Both topics share the same consumer group so each event is processed once.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionEventListener {

    private final NotificationService notificationService;

    @KafkaListener(
        topics = {KafkaTopics.TXN_COMPLETED, KafkaTopics.TXN_FAILED},
        groupId = KafkaTopics.GROUP_NOTIFICATION
    )
    public void onTransactionEvent(TransactionCompletedEvent event) {
        // TODO:
        // if "SUCCESS":
        //   notificationService.sendDebitAlert(senderPhone, event.getSenderUpiId(), event.getAmount(), txnId)
        //   notificationService.sendCreditAlert(receiverPhone, event.getReceiverUpiId(), event.getAmount(), txnId)
        // if "FAILED":
        //   notificationService.sendFailureAlert(senderPhone, event.getAmount(), event.getFailureReason())
        //
        // Note: You may need to enrich the event with phone numbers, or call user-service for them
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
