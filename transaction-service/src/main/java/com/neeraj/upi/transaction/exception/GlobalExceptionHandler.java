package com.neeraj.upi.transaction.exception;

import com.neeraj.upi.common.dto.ApiResponse;
import com.neeraj.upi.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException ex) {
        // TODO: same pattern as user-service and wallet-service
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        // TODO: collect field errors, return 400
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        // TODO: log + return 500
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
