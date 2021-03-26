package com.andreiai.tlchallenge.controller;

import com.andreiai.tlchallenge.domain.PokemonResponse;
import com.andreiai.tlchallenge.domain.exception.PokeProviderException;
import com.andreiai.tlchallenge.domain.exception.PokemonNotFoundException;
import com.andreiai.tlchallenge.domain.exception.ShakespeareProviderException;
import com.andreiai.tlchallenge.domain.exception.ShakespeareTooManyRequestsException;
import com.andreiai.tlchallenge.provider.PokeProvider;
import com.andreiai.tlchallenge.provider.ShakespeareProvider;
import com.andreiai.tlchallenge.service.PokemonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PokemonControllerTest {

    private static final String GET_POKEMON_SHAKESPEARE_PATH = "/pokemon/%s";
    private static final String POKEMON_NAME = "mewtwo";
    private static final String NORMAL_DESCRIPTION = "normal description";
    public static final String SHAKESPEARE_DESCRIPTION = "shakespeare description";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @Autowired
//    private PokemonService pokemonService;

    @MockBean
    private PokeProvider pokeProvider;
    @MockBean
    private ShakespeareProvider shakespeareProvider;

    @BeforeEach
    void setUp() {
        // pokemonService = new PokemonService(pokeProvider, shakespeareProvider);
        PokemonService.cachedShakespeare.remove(POKEMON_NAME);
    }

    @DisplayName("Get pokemon Shakespeare. Ok")
    @ParameterizedTest(name = "Getting shakespeare speech for {0}")
    @ValueSource(strings = { POKEMON_NAME })
    void getPokemonShakespeare_ok(String pokemonName) throws Exception {
        when(pokeProvider.getPokemonDescription(pokemonName)).thenReturn(NORMAL_DESCRIPTION);
        when(shakespeareProvider.getShakespeareDescription(NORMAL_DESCRIPTION)).thenReturn(SHAKESPEARE_DESCRIPTION);

        MvcResult mvcResult = mockMvc.perform(get(String.format(GET_POKEMON_SHAKESPEARE_PATH, pokemonName)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
        assertEquals(new PokemonResponse(pokemonName, SHAKESPEARE_DESCRIPTION), objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PokemonResponse.class));

        verify(pokeProvider).getPokemonDescription(pokemonName);
        verify(shakespeareProvider).getShakespeareDescription(NORMAL_DESCRIPTION);
        verifyNoMoreInteractions(pokeProvider);
        verifyNoMoreInteractions(shakespeareProvider);
    }

    @DisplayName("Get pokemon Shakespeare. Not Found")
    @ParameterizedTest(name = "Getting shakespeare speech for {0}")
    @ValueSource(strings = { POKEMON_NAME })
    void getPokemonShakespeare_notFound(String pokemonName) throws Exception {
        when(pokeProvider.getPokemonDescription(pokemonName)).thenThrow(new PokemonNotFoundException("Test", new Exception()));

        mockMvc.perform(get(String.format(GET_POKEMON_SHAKESPEARE_PATH, pokemonName)))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(pokeProvider).getPokemonDescription(pokemonName);
        verifyNoMoreInteractions(pokeProvider);
        verifyNoInteractions(shakespeareProvider);
    }

    @DisplayName("Get pokemon Shakespeare. Too Many Requests")
    @ParameterizedTest(name = "Getting shakespeare speech for {0}")
    @ValueSource(strings = { POKEMON_NAME })
    void getPokemonShakespeare_tooManyRequests(String pokemonName) throws Exception {
        when(pokeProvider.getPokemonDescription(pokemonName)).thenReturn(NORMAL_DESCRIPTION);
        when(shakespeareProvider.getShakespeareDescription(NORMAL_DESCRIPTION)).thenThrow(new ShakespeareTooManyRequestsException("test", new Exception()));

        mockMvc.perform(get(String.format(GET_POKEMON_SHAKESPEARE_PATH, pokemonName)))
                .andExpect(status().isTooManyRequests())
                .andReturn();

        verify(pokeProvider).getPokemonDescription(pokemonName);
        verify(shakespeareProvider).getShakespeareDescription(NORMAL_DESCRIPTION);
        verifyNoMoreInteractions(pokeProvider);
        verifyNoMoreInteractions(shakespeareProvider);
    }

    @DisplayName("Get pokemon Shakespeare. PokeAPI Service Unavailable")
    @ParameterizedTest(name = "Getting shakespeare speech for {0}")
    @ValueSource(strings = { POKEMON_NAME })
    void getPokemonShakespeare_PokeAPI_serviceUnavailable(String pokemonName) throws Exception {
        when(pokeProvider.getPokemonDescription(pokemonName)).thenThrow(new PokeProviderException("test", new Exception()));

        mockMvc.perform(get(String.format(GET_POKEMON_SHAKESPEARE_PATH, pokemonName)))
                .andExpect(status().isServiceUnavailable())
                .andReturn();

        verify(pokeProvider).getPokemonDescription(pokemonName);
        verifyNoMoreInteractions(pokeProvider);
        verifyNoInteractions(shakespeareProvider);
    }

    @DisplayName("Get pokemon Shakespeare. Shakespeare API Service Unavailable")
    @ParameterizedTest(name = "Getting shakespeare speech for {0}")
    @ValueSource(strings = { POKEMON_NAME })
    void getPokemonShakespeare_ShakespeareAPI_serviceUnavailable(String pokemonName) throws Exception {
        when(pokeProvider.getPokemonDescription(pokemonName)).thenReturn(NORMAL_DESCRIPTION);
        when(shakespeareProvider.getShakespeareDescription(NORMAL_DESCRIPTION)).thenThrow(new ShakespeareProviderException("test", new Exception()));

        mockMvc.perform(get(String.format(GET_POKEMON_SHAKESPEARE_PATH, pokemonName)))
                .andExpect(status().isServiceUnavailable())
                .andReturn();

        verify(pokeProvider).getPokemonDescription(pokemonName);
        verify(shakespeareProvider).getShakespeareDescription(NORMAL_DESCRIPTION);
        verifyNoMoreInteractions(pokeProvider);
        verifyNoMoreInteractions(shakespeareProvider);
    }
}