package com.neeraj.upi.wallet.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Observability, Metrics and Tracing Configuration for Wallet Service.
 * Configures application-specific Prometheus meters and tracing aspects.
 */
@Configuration
@Slf4j
public class ObservabilityConfig {

    // TODO:
    //  1. Define counters for wallet creation, money top-ups, and debit/credit actions.
    //  2. Configure a MeterFilter to avoid recording JVM metrics to save Prometheus memory.
    //  3. Register TimedAspect to track balance lookup and transfer latency under @Timed.
}
