package com.andreiai.tlchallenge.domain.exception;

public class ShakespeareBadRequestException extends BadRequestException {

    public ShakespeareBadRequestException(String message, Exception e) {
        super(message, e);
    }
}
