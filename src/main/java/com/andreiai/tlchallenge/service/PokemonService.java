package com.andreiai.tlchallenge.service;

import com.andreiai.tlchallenge.provider.PokeProvider;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {

    private final PokeProvider pokeProvider;

    public PokemonService(PokeProvider pokeProvider) {
        this.pokeProvider = pokeProvider;
    }

    public String getPokemonName(String pokemonName) {

        return pokeProvider.getPokemonDescription(pokemonName);
    }
}
