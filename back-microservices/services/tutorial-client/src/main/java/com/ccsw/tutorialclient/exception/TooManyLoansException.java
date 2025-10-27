package com.ccsw.tutorialclient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class TooManyLoansException extends RuntimeException {
    public TooManyLoansException(String message) {
        super(message);
    }
}