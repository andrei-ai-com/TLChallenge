package com.andreiai.tlchallenge;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApplicationTest {

    @Test
    @DisplayName("Running a sanity check")
    void sanity_check() {
        assertTrue(true);
    }
}