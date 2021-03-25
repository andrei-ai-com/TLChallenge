package com.andreiai.tlchallenge.provider;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ShakespeareProvider {

    private static final String location = "https://api.funtranslations.com/translate/";
    private static final String SHAKESPEARE_PATH = "shakespeare";

    private final RestTemplate restTemplate;

    public ShakespeareProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getShakespeareDescription(String description) {

        return description;
    }
}
