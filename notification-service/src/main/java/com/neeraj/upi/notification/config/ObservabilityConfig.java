package com.neeraj.upi.notification.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Observability, Metrics and Tracing Configuration for Notification Service.
 * Configures Kafka listener metrics and async trace span interceptors.
 */
@Configuration
@Slf4j
public class ObservabilityConfig {

    // TODO:
    //  1. Define a counter to monitor total email and SMS alerts sent successfully or failed.
    //  2. Register TimedAspect to track Kafka listener message consumption latency.
    //  3. Configure tracing propagation for asynchronous Kafka listeners to seamlessly bind spans.
}
