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
        // TODO: return ResponseEntity.status(ex.getHttpStatus()).body(ApiResponse.fail(...))
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** Handle @Valid bean validation errors */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        // TODO: collect field errors, return 400 with VALIDATION_ERROR code
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** Handle bad login credentials */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        // TODO: return 401 INVALID_CREDENTIALS
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** Catch-all for unhandled exceptions */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        // TODO: log + return 500 INTERNAL_ERROR
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
