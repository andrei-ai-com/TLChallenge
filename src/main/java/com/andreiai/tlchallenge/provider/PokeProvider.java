package com.andreiai.tlchallenge.provider;

import com.andreiai.tlchallenge.domain.PokemonSpecie;
import com.andreiai.tlchallenge.domain.exception.PokeProviderException;
import com.andreiai.tlchallenge.domain.exception.PokemonNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
public class PokeProvider {

    private static final String location = "https://pokeapi.co/api/v2/";
    private static final String POKEMON_SPECIES_PATH = "pokemon-species/%s";

    private final RestTemplate restTemplate;

    public PokeProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getPokemonDescription(String pokemonName) {

        String url = UriComponentsBuilder.fromHttpUrl(location + String.format(POKEMON_SPECIES_PATH, pokemonName))
                .toUriString();

        try {
            return Objects.requireNonNull(restTemplate.getForObject(
                    url,
                    PokemonSpecie.class
            )).getTextEntries().stream()
                    .filter(flavourTextEntry -> flavourTextEntry.getLanguage().getName().equals("en"))
                    .findFirst()
                    .orElseThrow(() -> new PokemonNotFoundException(String.format("We could not find any pokemon with the name %s", pokemonName), new Exception()))
                    .getText();
        } catch (HttpClientErrorException e) {
            throw new PokemonNotFoundException(String.format("We could not find any pokemon with the name %s", pokemonName), e);
        } catch (HttpServerErrorException e) {
            throw new PokeProviderException("The PokeAPI is currently unavailable", e);
        }
    }
}
