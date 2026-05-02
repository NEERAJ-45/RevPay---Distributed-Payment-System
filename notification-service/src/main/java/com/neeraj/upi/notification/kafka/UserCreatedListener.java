package com.neeraj.upi.notification.kafka;

import com.neeraj.upi.common.constants.KafkaTopics;
import com.neeraj.upi.notification.dto.UserCreatedEvent;
import com.neeraj.upi.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Listens to user.created events and sends a welcome notification.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedListener {

    private final NotificationService notificationService;

    @KafkaListener(
        topics = KafkaTopics.USER_CREATED,
        groupId = KafkaTopics.GROUP_NOTIFICATION
    )
    public void onUserCreated(UserCreatedEvent event) {
        // TODO:
        // 1. log.info("Received UserCreated event for upiId={}", event.getUpiId())
        // 2. notificationService.sendWelcome(event.getUpiId(), event.getFullName(), event.getPhone())
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
