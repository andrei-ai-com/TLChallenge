package com.andreiai.tlchallenge.provider;

import com.andreiai.tlchallenge.domain.ShakespeareResponse;
import com.andreiai.tlchallenge.domain.exception.ShakespeareBadRequestException;
import com.andreiai.tlchallenge.domain.exception.ShakespeareProviderException;
import com.andreiai.tlchallenge.domain.exception.ShakespeareTooManyRequestsException;
import com.andreiai.tlchallenge.service.PokemonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
public class ShakespeareProvider {

    private static final Logger logger = LoggerFactory.getLogger(ShakespeareProvider.class);

    private static final String location = "https://api.funtranslations.com/translate/";
    private static final String SHAKESPEARE_PATH = "shakespeare.json";

    private final RestTemplate restTemplate;

    public ShakespeareProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getShakespeareDescription(String description) {

        String url = UriComponentsBuilder.fromHttpUrl(location + SHAKESPEARE_PATH)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> urlData = new LinkedMultiValueMap<>();
        urlData.add("text", description);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(urlData, headers);

        logger.info("Calling Shakespeare API: {} - {}", url, description);

        try {
            return Objects.requireNonNull(restTemplate.exchange(url,
                    HttpMethod.POST,
                    entity,
                    ShakespeareResponse.class).getBody()).getContents().getTranslated();
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new ShakespeareTooManyRequestsException(String.format("%s --> Here is a list of cached Shakespeare pokemons %s", e.getMessage(), PokemonService.cachedShakespeare), e);
        } catch (HttpClientErrorException e) {
            throw new ShakespeareBadRequestException(e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            throw new ShakespeareProviderException("The ShakespeareAPI is currently unavailable", e);
        }
    }
}
