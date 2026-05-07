package com.neeraj.upi.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Global JWT validation filter — runs BEFORE every route.
 *
 * Logic:
 *  - If the path is a PUBLIC endpoint (/auth/**) → skip validation, pass through.
 *  - Otherwise → extract "Authorization: Bearer <token>", validate JWT.
 *  - If invalid → return 401 Unauthorized immediately.
 *  - If valid   → forward request downstream.
 *
 * NOTE: This is a reactive (WebFlux) filter — do NOT use blocking calls here.
 */
@Component
@Slf4j
public class JwtAuthFilter implements GlobalFilter, Ordered {


    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // TODO:
        // 1. Check if path matches any PUBLIC_PATHS → if yes, chain.filter(exchange)
        // 2. Extract Authorization header
        // 3. If missing or doesn't start with "Bearer " → return 401
        // 4. Validate token using JJWT (same logic as user-service JwtService)
        // 5. If valid → chain.filter(exchange)
        // 6. If invalid → exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED) + complete()

        return chain.filter(exchange); // placeholder — remove when implementing
    }

    @Override
    public int getOrder() {
        // Run before all other filters (lower number = higher priority)
        return -1;
    }
}
