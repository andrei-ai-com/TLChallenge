package com.andreiai.tlchallenge.controller;

import com.andreiai.tlchallenge.domain.PokemonResponse;
import com.andreiai.tlchallenge.domain.exception.BadRequestException;
import com.andreiai.tlchallenge.domain.exception.PokemonNotFoundException;
import com.andreiai.tlchallenge.domain.exception.ServiceUnavailableException;
import com.andreiai.tlchallenge.service.PokemonService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonController.class);

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping(value = "{pokemonName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PokemonResponse.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 503, message = "Service Unavailable")
    })
    public ResponseEntity getHealthcheck(@PathVariable String pokemonName) {

        if (pokemonName == null || pokemonName.isBlank()) {
            return new ResponseEntity<>("Please provide us with a Pokemon name", HttpStatus.BAD_REQUEST);
        }

        try {
            return new ResponseEntity<>(pokemonService.getPokemonName(pokemonName), HttpStatus.OK);
        } catch (BadRequestException e) {
            logger.info("Bad request", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (PokemonNotFoundException e) {
            logger.info("Pokemon not found", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ServiceUnavailableException e) {
            logger.warn("One of the third parties is unavailable", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            logger.error("Internal server error", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
