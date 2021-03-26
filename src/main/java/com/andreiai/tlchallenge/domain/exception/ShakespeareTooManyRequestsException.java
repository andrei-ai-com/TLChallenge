package com.andreiai.tlchallenge.domain.exception;

public class ShakespeareTooManyRequestsException extends TooManyRequestsException {

    public ShakespeareTooManyRequestsException(String message, Exception e) {
        super(message, e);
    }
}
