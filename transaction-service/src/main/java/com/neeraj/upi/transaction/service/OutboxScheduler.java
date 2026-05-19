package com.neeraj.upi.transaction.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Background scheduler that implements the Outbox Pattern for Transactions.
 * It periodically polls the database for unprocessed events, publishes them to Kafka,
 * and marks them as processed to ensure transactional reliability and at-least-once delivery.
 */
@Component
@EnableScheduling
@Slf4j
public class OutboxScheduler {

    @Scheduled(fixedDelay = 2000) // Runs every 2 seconds after the last execution completes
    public void processOutboxEvents() {
        // TODO: Poll unprocessed outbox events from the outbox_events table,
        //       serialize/deserialize payloads, publish to the correct Kafka topic via TransactionEventPublisher,
        //       and update the processed status in the database inside a transactional block.
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
