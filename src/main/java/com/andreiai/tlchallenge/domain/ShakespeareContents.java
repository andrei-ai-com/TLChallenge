package com.andreiai.tlchallenge.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShakespeareContents {

    @JsonProperty("translated")
    private String translated;

    public ShakespeareContents() {
    }

    public ShakespeareContents(String translated) {
        this.translated = translated;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShakespeareContents that = (ShakespeareContents) o;
        return Objects.equals(translated, that.translated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translated);
    }

    @Override
    public String toString() {
        return "ShakespeareContents{" +
                "translated='" + translated + '\'' +
                '}';
    }
}
