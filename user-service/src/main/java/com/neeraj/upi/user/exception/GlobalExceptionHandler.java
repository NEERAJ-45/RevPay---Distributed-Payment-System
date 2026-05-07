package com.neeraj.upi.user.exception;

import com.neeraj.upi.common.dto.ApiResponse;
import com.neeraj.upi.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /** Handle business exceptions (UserAlreadyExists, UserNotFound, etc.) */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException ex) {
        log.error("Business exception: {} - {}", ex.getErrorCode(), ex.getMessage());
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(ApiResponse.fail(ex.getErrorCode(), ex.getMessage()));
    }

    /** Handle @Valid bean validation errors */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");
        
        log.error("Validation error: {}", errorMessage);
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail("VALIDATION_ERROR", errorMessage));
    }

    /** Handle bad login credentials */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return ResponseEntity
                .status(401)
                .body(ApiResponse.fail("INVALID_CREDENTIALS", "Invalid phone number or PIN"));
    }

    /** Catch-all for unhandled exceptions */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        log.error("Unhandled exception: ", ex);
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.fail("INTERNAL_ERROR", "An unexpected error occurred"));
    }
}
