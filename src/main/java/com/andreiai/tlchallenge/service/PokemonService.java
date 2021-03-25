package com.andreiai.tlchallenge.service;

import com.andreiai.tlchallenge.domain.PokemonResponse;
import com.andreiai.tlchallenge.provider.PokeProvider;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {

    private final PokeProvider pokeProvider;

    public PokemonService(PokeProvider pokeProvider) {
        this.pokeProvider = pokeProvider;
    }

    public PokemonResponse getPokemonName(String pokemonName) {

        String description = curateDescription(pokeProvider.getPokemonDescription(pokemonName));

        return new PokemonResponse(pokemonName, description);
    }

    private String curateDescription(String pokemonDescription) {
        return pokemonDescription.replaceAll("\n", " ");
    }
}
