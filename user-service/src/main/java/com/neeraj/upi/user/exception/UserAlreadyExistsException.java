package com.neeraj.upi.user.exception;

import com.neeraj.upi.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BaseException {
    public UserAlreadyExistsException(String message) {
        super("USER_ALREADY_EXISTS", message, HttpStatus.CONFLICT);
    }
}
