package com.andreiai.tlchallenge.domain.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message, Exception e) {
        super(message, e);
    }
}
