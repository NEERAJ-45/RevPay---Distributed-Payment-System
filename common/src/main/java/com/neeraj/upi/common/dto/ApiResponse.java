package com.neeraj.upi.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Standard API response envelope for all services.
 * <pre>
 * Success: { "success": true,  "data": {...},  "error": null,  "timestamp": "..." }
 * Failure: { "success": false, "data": null,   "error": {...}, "timestamp": "..." }
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private ErrorDetails error;
    private Instant timestamp;

    // ── Convenience factory methods ──────────────────────────────────────────

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(new ErrorDetails(code, message))
                .timestamp(Instant.now())
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorDetails {
        private String code;
        private String message;
    }
}
