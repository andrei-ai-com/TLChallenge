package com.andreiai.tlchallenge.service;

import com.andreiai.tlchallenge.domain.PokemonResponse;
import com.andreiai.tlchallenge.domain.exception.PokemonNotFoundException;
import com.andreiai.tlchallenge.provider.PokeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    private static final String POKEMON_NAME = "pikachu";

    @Mock
    private PokeProvider pokeProvider;

    private PokemonService pokemonService;

    @BeforeEach
    void setUp() {
        pokemonService = new PokemonService(pokeProvider);
    }

    @Test
    @DisplayName("Get pokemon description. Ok")
    public void checkPokemonGetsDescription() {
        when(pokeProvider.getPokemonDescription(POKEMON_NAME)).thenReturn("Quite\nshocking");

        PokemonResponse pokemonResponse = pokemonService.getPokemonName(POKEMON_NAME);

        assertEquals(pokemonResponse.getName(), "pikachu");
        assertEquals(pokemonResponse.getDescription(), "Quite shocking");
    }

    @Test
    @DisplayName("Get pokemon description. PokeAPI failure. Not Found")
    public void checkPokemonGetsDescription_NotFound() {
        when(pokeProvider.getPokemonDescription(POKEMON_NAME)).thenThrow(new PokemonNotFoundException("Test"));

        try {
            pokemonService.getPokemonName(POKEMON_NAME);
            fail();
        } catch (PokemonNotFoundException ignored) {}
    }
}