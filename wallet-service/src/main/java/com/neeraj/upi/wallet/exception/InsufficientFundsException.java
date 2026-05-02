package com.neeraj.upi.wallet.exception;

import com.neeraj.upi.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InsufficientFundsException extends BaseException {
    public InsufficientFundsException(String upiId, String amount) {
        super("INSUFFICIENT_FUNDS",
              "Wallet " + upiId + " has insufficient balance for ₹" + amount,
              HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
