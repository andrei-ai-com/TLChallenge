package com.andreiai.tlchallenge.domain.exception;

public class PokemonNotFoundException extends RuntimeException {

    public PokemonNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
