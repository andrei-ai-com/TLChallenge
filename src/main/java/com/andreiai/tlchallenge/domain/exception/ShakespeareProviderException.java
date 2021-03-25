package com.andreiai.tlchallenge.domain.exception;

public class ShakespeareProviderException extends ServiceUnavailableException {

    public ShakespeareProviderException(String message, Exception e) {
        super(message, e);
    }
}
