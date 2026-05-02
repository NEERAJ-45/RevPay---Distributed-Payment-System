package com.neeraj.upi.wallet.exception;

import com.neeraj.upi.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class WalletNotFoundException extends BaseException {
    public WalletNotFoundException(String upiId) {
        super("WALLET_NOT_FOUND", "No wallet found for UPI ID: " + upiId, HttpStatus.NOT_FOUND);
    }
}
