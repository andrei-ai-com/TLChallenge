package com.andreiai.tlchallenge.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class HealthcheckController {

    private static final Logger logger = LoggerFactory.getLogger(HealthcheckController.class);

    public static final String OK_STRING = "OK";
    public static final String ILL_STRING = "ILL";

    @GetMapping(value = "", produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = String.class),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<String> getHealthcheck() {
        try {
            return new ResponseEntity<>(OK_STRING, HttpStatus.OK);
        } catch(Exception e) {
            logger.error("HEALTHCHECK: {}", ILL_STRING);

            return new ResponseEntity<>(ILL_STRING, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
