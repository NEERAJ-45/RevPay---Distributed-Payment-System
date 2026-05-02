package com.neeraj.upi.common.constants;

/**
 * Shared Kafka topic names across all services.
 * Single source of truth — never hard-code topic strings in services.
 */
public final class KafkaTopics {

    private KafkaTopics() {}

    // User Service publishes
    public static final String USER_CREATED = "user.created";

    // Transaction Service publishes
    public static final String TXN_COMPLETED = "txn.completed";
    public static final String TXN_FAILED    = "txn.failed";

    // Consumer group IDs
    public static final String GROUP_WALLET       = "wallet-service-group";
    public static final String GROUP_NOTIFICATION = "notification-service-group";
}
