package com.neeraj.upi.transaction.exception;

import com.neeraj.upi.common.exception.BaseException;
import org.springframework.http.HttpStatus;

/** Thrown when a transaction is submitted but the sender has insufficient balance */
public class InsufficientFundsException extends BaseException {
    public InsufficientFundsException() {
        super("INSUFFICIENT_FUNDS", "Sender wallet has insufficient balance", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
