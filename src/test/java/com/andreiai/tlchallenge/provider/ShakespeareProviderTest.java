package com.andreiai.tlchallenge.provider;

import com.andreiai.tlchallenge.domain.exception.PokeProviderException;
import com.andreiai.tlchallenge.domain.exception.PokemonNotFoundException;
import com.andreiai.tlchallenge.domain.exception.ShakespeareProviderException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@SpringBootTest
class ShakespeareProviderTest {

    private static final String SHAKESPEARE_LOCATION = "https://api.funtranslations.com/translate/";
    private static final String SHAKESPEARE_SPEECH_PATH = "shakespeare";

    private static final String EXAMPLE_TEXT = "You gave Mr. Tim a hearty meal, but unfortunately what he ate made him die.";

    @Autowired
    private ShakespeareProvider shakespeareProvider;
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Value("classpath:shakespeare_ok.json")
    Resource shakespeareOkResource;

    @DisplayName("Check Shakespeare Speech")
    @ParameterizedTest(name = "Getting Shakespeare speech for {0}")
    @ValueSource(strings = { EXAMPLE_TEXT })
    public void checkPokeAPI_ok(String text) {

        URI uri = UriComponentsBuilder.fromHttpUrl(SHAKESPEARE_LOCATION + SHAKESPEARE_SPEECH_PATH)
                .queryParam(text)
                .build()
                .toUri();

        mockServer.expect(ExpectedCount.once(), requestTo(uri))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withSuccess(
                                shakespeareOkResource,
                                MediaType.APPLICATION_JSON)
                );

        String shakespeareSpeech = shakespeareProvider.getShakespeareDescription(text);

        assertEquals("Thee did giveth mr. Tim a hearty meal,  but unfortunately what he did doth englut did maketh him kicketh the bucket.", shakespeareSpeech);

        mockServer.verify();
    }

    @DisplayName("Check Shakespeare Speech, API error")
    @ParameterizedTest(name = "Getting Shakespeare speech for {0}")
    @ValueSource(strings = { EXAMPLE_TEXT })
    public void checkPokeAPI_not_found(String text) {

        URI uri = UriComponentsBuilder.fromHttpUrl(SHAKESPEARE_LOCATION + SHAKESPEARE_SPEECH_PATH)
                .queryParam(text)
                .build()
                .toUri();

        mockServer.expect(ExpectedCount.once(), requestTo(uri))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());

        try {
            shakespeareProvider.getShakespeareDescription(text);
            fail();
        } catch (ShakespeareProviderException e) {
            assertEquals("The ShakespeareAPI is currently unavailable", e.getMessage());
        }

        mockServer.verify();
    }
}