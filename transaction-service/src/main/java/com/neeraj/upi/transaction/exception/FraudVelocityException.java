package com.neeraj.upi.transaction.exception;

import com.neeraj.upi.common.exception.BaseException;
import org.springframework.http.HttpStatus;

/** Thrown when fraud velocity checks fail (daily limit, amount cap, self-pay) */
public class FraudVelocityException extends BaseException {
    public FraudVelocityException(String code, String message) {
        super(code, message, HttpStatus.TOO_MANY_REQUESTS);
    }
}
