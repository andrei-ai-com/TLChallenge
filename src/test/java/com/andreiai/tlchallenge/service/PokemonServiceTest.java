package com.andreiai.tlchallenge.service;

import com.andreiai.tlchallenge.domain.PokemonResponse;
import com.andreiai.tlchallenge.domain.exception.PokeProviderException;
import com.andreiai.tlchallenge.domain.exception.PokemonNotFoundException;
import com.andreiai.tlchallenge.domain.exception.ServiceUnavailableException;
import com.andreiai.tlchallenge.domain.exception.ShakespeareProviderException;
import com.andreiai.tlchallenge.provider.PokeProvider;
import com.andreiai.tlchallenge.provider.ShakespeareProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    private static final String POKEMON_NAME = "pikachu";

    @Mock
    private PokeProvider pokeProvider;
    @Mock
    private ShakespeareProvider shakespeareProvider;

    private PokemonService pokemonService;

    @BeforeEach
    void setUp() {
        pokemonService = new PokemonService(pokeProvider, shakespeareProvider);
    }

    @Test
    @DisplayName("Get pokemon description. Ok")
    public void checkPokemonGetsDescription() {
        when(pokeProvider.getPokemonDescription(POKEMON_NAME)).thenReturn("Quite\nshocking");
        when(shakespeareProvider.getShakespeareDescription(anyString())).thenReturn("Quite shocking");

        PokemonResponse pokemonResponse = pokemonService.getPokemonName(POKEMON_NAME);

        assertEquals("pikachu", pokemonResponse.getName());
        assertEquals("Quite shocking", pokemonResponse.getDescription());
    }

    @Test
    @DisplayName("Get pokemon description. PokeAPI failure. Not Found")
    public void checkPokemonGetsDescription_NotFound() {
        when(pokeProvider.getPokemonDescription(POKEMON_NAME)).thenThrow(new PokemonNotFoundException("Test", new Exception()));

        try {
            pokemonService.getPokemonName(POKEMON_NAME);
            fail();
        } catch (PokemonNotFoundException ignored) {}
    }

    @Test
    @DisplayName("Get pokemon description. Poke API failure. Service Unavailable")
    public void checkPokemonGetsDescription_PokeAPIFailure_ServiceUnavailable() {
        when(pokeProvider.getPokemonDescription(POKEMON_NAME)).thenThrow(new PokeProviderException("Test", new Exception()));

        try {
            pokemonService.getPokemonName(POKEMON_NAME);
            fail();
        } catch (ServiceUnavailableException ignored) {}
    }

    @Test
    @DisplayName("Get pokemon description. Shakespeare API failure. Service Unavailable")
    public void checkPokemonGetsDescription_ShakespeareFailure_ServiceUnavailable() {
        when(pokeProvider.getPokemonDescription(POKEMON_NAME)).thenReturn("Quite\nshocking");
        when(shakespeareProvider.getShakespeareDescription(anyString())).thenThrow(new ShakespeareProviderException("Test", new Exception()));

        try {
            pokemonService.getPokemonName(POKEMON_NAME);
            fail();
        } catch (ServiceUnavailableException ignored) {}
    }
}