package com.andreiai.tlchallenge.domain.exception;

public class TooManyRequestsException extends RuntimeException {

    public TooManyRequestsException(String message, Exception e) {
        super(message, e);
    }
}
