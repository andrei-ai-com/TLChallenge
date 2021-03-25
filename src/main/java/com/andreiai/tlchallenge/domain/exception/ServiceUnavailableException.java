package com.andreiai.tlchallenge.domain.exception;

public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException(String message, Exception e) {
        super(message, e);
    }
}
