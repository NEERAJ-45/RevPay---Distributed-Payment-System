package com.neeraj.upi.user.kafka;

import com.neeraj.upi.common.constants.KafkaTopics;
import com.neeraj.upi.user.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher {

    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    /**
     * Publishes a UserCreated event to Kafka topic: user.created
     * Key = userId (ensures ordering per user on same partition)
     */
    public void publishUserCreated(UserCreatedEvent event) {
        // TODO: kafkaTemplate.send(KafkaTopics.USER_CREATED, event.getUserId().toString(), event)
        //       .whenComplete((result, ex) -> log success/failure)
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
