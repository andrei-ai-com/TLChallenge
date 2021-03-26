package com.andreiai.tlchallenge.provider;

import com.andreiai.tlchallenge.domain.exception.BadRequestException;
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
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@SpringBootTest
class ShakespeareProviderTest {

    private static final String SHAKESPEARE_LOCATION = "https://api.funtranslations.com/translate/";
    private static final String SHAKESPEARE_SPEECH_PATH = "shakespeare.json";

    private static final String EXAMPLE_TEXT = "original";

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
    public void checkShakespeareAPI_ok(String text) {

        URI uri = UriComponentsBuilder.fromHttpUrl(SHAKESPEARE_LOCATION + SHAKESPEARE_SPEECH_PATH)
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

        assertEquals("translated", shakespeareSpeech);

        mockServer.verify();
    }

    @DisplayName("Check Shakespeare Speech, API error")
    @ParameterizedTest(name = "Getting Shakespeare speech for {0}")
    @ValueSource(strings = { EXAMPLE_TEXT })
    public void checkShakespeareAPI_serverError(String text) {

        URI uri = UriComponentsBuilder.fromHttpUrl(SHAKESPEARE_LOCATION + SHAKESPEARE_SPEECH_PATH)
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

    @DisplayName("Check Shakespeare Speech, Bad Request error")
    @ParameterizedTest(name = "Getting Shakespeare speech for {0}")
    @ValueSource(strings = { EXAMPLE_TEXT })
    public void checkShakespeareAPI_badRequest(String text) {

        URI uri = UriComponentsBuilder.fromHttpUrl(SHAKESPEARE_LOCATION + SHAKESPEARE_SPEECH_PATH)
                .build()
                .toUri();

        mockServer.expect(ExpectedCount.once(), requestTo(uri))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withBadRequest());

        try {
            shakespeareProvider.getShakespeareDescription(text);
            fail();
        } catch (BadRequestException ignored) {}

        mockServer.verify();
    }
}