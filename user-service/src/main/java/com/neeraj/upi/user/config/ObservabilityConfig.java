package com.neeraj.upi.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Observability, Metrics and Tracing Configuration for User Service.
 * Configures application-specific Prometheus meters and tracing aspects.
 */
@Configuration
@Slf4j
public class ObservabilityConfig {

    // TODO:
    //  1. Inject MeterRegistry to set global registry tags (e.g. env = local, region = default).
    //  2. Define custom Counter/Timer beans for user registration and authentication performance.
    //  3. Register TimedAspect to allow using @Timed on service methods.
}
