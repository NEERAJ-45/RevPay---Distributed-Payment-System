package com.neeraj.upi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base exception for all UPI service business exceptions.
 * Carry an HTTP status and a machine-readable error code.
 */
@Getter
public class BaseException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;

    public BaseException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode  = errorCode;
        this.httpStatus = httpStatus;
    }

    public BaseException(String errorCode, String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode  = errorCode;
        this.httpStatus = httpStatus;
    }
}
