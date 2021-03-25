package com.andreiai.tlchallenge.provider;

import com.andreiai.tlchallenge.domain.exception.PokeProviderException;
import com.andreiai.tlchallenge.domain.exception.PokemonNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class PokeProviderTest {

    private static final String POKEAPI_LOCATION = "https://pokeapi.co/api/v2/";
    private static final String POKEMON_SPECIES_PATH = "pokemon-species/%s";

    private static final String POKEMON_NAME = "charizard";
    private static final String POKEMON_NAME_NOT_FOUND = "pikachu";

    @Autowired
    private PokeProvider pokeProvider;
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Value("classpath:charizard_specie.json")
    Resource charizardSpecieResource;

    @DisplayName("Get pokemon description. Ok")
    @ParameterizedTest(name = "Getting description for {0}")
    @ValueSource(strings = { POKEMON_NAME })
    public void checkPokeAPI_ok(String pokemonName) {

        URI uri = UriComponentsBuilder.fromHttpUrl(POKEAPI_LOCATION + String.format(POKEMON_SPECIES_PATH, pokemonName))
                .build()
                .toUri();



        mockServer.expect(ExpectedCount.once(), requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withSuccess(
                                charizardSpecieResource,
                                MediaType.APPLICATION_JSON)
                );

        String pokemonDescription = pokeProvider.getPokemonDescription(pokemonName);

        assertEquals("Spits fire that\n" +
                "is hot enough to\n" +
                "melt boulders.\fKnown to cause\n" +
                "forest fires\n" +
                "unintentionally.", pokemonDescription);

        mockServer.verify();
    }

    @DisplayName("Get pokemon description. Not Found")
    @ParameterizedTest(name = "Getting description for {0}")
    @ValueSource(strings = { POKEMON_NAME_NOT_FOUND })
    public void checkPokeAPI_not_found(String pokemonName) {

        URI uri = UriComponentsBuilder.fromHttpUrl(POKEAPI_LOCATION + String.format(POKEMON_SPECIES_PATH, pokemonName))
                .build()
                .toUri();

        mockServer.expect(ExpectedCount.once(), requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        try {
            pokeProvider.getPokemonDescription(pokemonName);
            fail();
        } catch (PokemonNotFoundException e) {
            assertEquals(String.format("We could not find any pokemon with the name %s", pokemonName.toLowerCase()), e.getMessage());
        }

        mockServer.verify();
    }

    @DisplayName("Get pokemon description. Server Error")
    @ParameterizedTest(name = "Getting description for {0}")
    @ValueSource(strings = { POKEMON_NAME_NOT_FOUND })
    public void checkPokeAPI_serverError(String pokemonName) {

        URI uri = UriComponentsBuilder.fromHttpUrl(POKEAPI_LOCATION + String.format(POKEMON_SPECIES_PATH, pokemonName))
                .build()
                .toUri();

        mockServer.expect(ExpectedCount.once(), requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        try {
            pokeProvider.getPokemonDescription(pokemonName);
            fail();
        } catch (PokeProviderException e) {
            assertEquals("The PokeAPI is currently unavailable", e.getMessage());
        }

        mockServer.verify();
    }
}