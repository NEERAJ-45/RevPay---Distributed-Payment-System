package com.neeraj.upi.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Observability, Metrics and Tracing Configuration for API Gateway.
 * Configures Prometheus metrics and trace parent headers validation/propagation.
 */
@Configuration
@Slf4j
public class ObservabilityConfig {

    // TODO:
    //  1. Define a KeyResolver or custom meter registry tags to classify API Gateway requests by Client IP or Route ID.
    //  2. Register a TimedAspect bean for supporting Micrometer's @Timed annotation on gateway routes.
    //  3. Configure custom trace span filters to skip health-check endpoint tracing to reduce telemetry noise.
}
