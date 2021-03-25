package com.andreiai.tlchallenge.domain.exception;

public class PokeProviderException extends ServiceUnavailableException {

    public PokeProviderException(String message, Exception e) {
        super(message, e);
    }
}
