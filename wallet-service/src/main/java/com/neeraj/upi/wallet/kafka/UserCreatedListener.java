package com.neeraj.upi.wallet.kafka;

import com.neeraj.upi.common.constants.KafkaTopics;
import com.neeraj.upi.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 *
 * Listens to the user.created Kafka topic and auto-creates a wallet.
 * This decouples wallet creation from user registration — eventual consistency.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedListener {

    private final WalletService walletService;

    @KafkaListener(topics = KafkaTopics.USER_CREATED, groupId = KafkaTopics.GROUP_WALLET)
    public void onUserCreated(Object event) {
        // TODO:
        // 1. Cast event to UserCreatedEvent (or use @Payload with proper deserializer)
        // 2. log.info("Received UserCreated for upiId={}", event.getUpiId())
        // 3. walletService.createWallet(event.getUserId(), event.getUpiId())
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
