package com.neeraj.upi.transaction.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Observability, Metrics and Tracing Configuration for Transaction Service.
 * Configures payment orchestration tracking, success/failure ratios, and latency timers.
 */
@Configuration
@Slf4j
public class ObservabilityConfig {

    // TODO:
    //  1. Inject MeterRegistry to register core payment metrics:
    //     - counter: "upi.payments.count" tagged with status (SUCCESS, FAILED)
    //     - timer: "upi.payments.latency" to measure orchestration processing speed
    //  2. Register TimedAspect to track fraud engine processing latency.
    //  3. Configure OpenTelemetry span customized options to tag payments with transaction/request IDs.
}
