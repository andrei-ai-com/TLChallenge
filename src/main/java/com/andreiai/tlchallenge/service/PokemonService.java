package com.andreiai.tlchallenge.service;

import com.andreiai.tlchallenge.domain.PokemonResponse;
import com.andreiai.tlchallenge.provider.PokeProvider;
import com.andreiai.tlchallenge.provider.ShakespeareProvider;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {

    private final PokeProvider pokeProvider;
    private final ShakespeareProvider shakespeareProvider;

    public PokemonService(PokeProvider pokeProvider, ShakespeareProvider shakespeareProvider) {
        this.pokeProvider = pokeProvider;
        this.shakespeareProvider = shakespeareProvider;
    }

    public PokemonResponse getPokemonName(String pokemonName) {

        pokemonName = pokemonName.toLowerCase();

        String description = curateDescription(pokeProvider.getPokemonDescription(pokemonName));
        String shakespeareDescription = shakespeareProvider.getShakespeareDescription(description);

        return new PokemonResponse(pokemonName, shakespeareDescription);
    }

    private String curateDescription(String pokemonDescription) {
        return pokemonDescription.replaceAll("\n", " ");
    }
}
