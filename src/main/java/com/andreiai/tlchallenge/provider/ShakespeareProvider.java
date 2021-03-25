package com.andreiai.tlchallenge.provider;

import com.andreiai.tlchallenge.domain.ShakespeareResponse;
import com.andreiai.tlchallenge.domain.exception.ShakespeareProviderException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
public class ShakespeareProvider {

    private static final String location = "https://api.funtranslations.com/translate/";
    private static final String SHAKESPEARE_PATH = "shakespeare";

    private final RestTemplate restTemplate;

    public ShakespeareProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getShakespeareDescription(String description) {

        String url = UriComponentsBuilder.fromHttpUrl(location + SHAKESPEARE_PATH)
                .queryParam("text", description)
                .toUriString();

        try {
            return Objects.requireNonNull(restTemplate.postForObject(
                    url,
                    null,
                    ShakespeareResponse.class
            )).getContents().getTranslated();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ShakespeareProviderException("The ShakespeareAPI is currently unavailable");
        }
    }
}
