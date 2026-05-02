package com.neeraj.upi.user.exception;

import com.neeraj.upi.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super("USER_NOT_FOUND", message, HttpStatus.NOT_FOUND);
    }
}
