package com.neeraj.upi.transaction.kafka;

import com.neeraj.upi.common.constants.KafkaTopics;
import com.neeraj.upi.transaction.event.TransactionCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes transaction outcome events to Kafka.
 * Consumed by notification-service for debit/credit SMS alerts.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionEventPublisher {

    private final KafkaTemplate<String, TransactionCompletedEvent> kafkaTemplate;

    /**
     * Publishes a txn.completed (SUCCESS) or txn.failed event.
     * Key = txnId (ordering per transaction).
     */
    public void publish(TransactionCompletedEvent event) {
        // TODO: determine topic from event.getStatus() ("SUCCESS" → TXN_COMPLETED, "FAILED" → TXN_FAILED)
        //       kafkaTemplate.send(topic, event.getTxnId().toString(), event)
        //       .whenComplete((result, ex) -> log success/failure)
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
