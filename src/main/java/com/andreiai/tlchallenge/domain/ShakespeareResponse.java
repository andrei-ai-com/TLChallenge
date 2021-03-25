package com.andreiai.tlchallenge.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShakespeareResponse {

    @JsonProperty("contents")
    private ShakespeareContents contents;

    public ShakespeareResponse() {
    }

    public ShakespeareResponse(ShakespeareContents contents) {
        this.contents = contents;
    }

    public ShakespeareContents getContents() {
        return contents;
    }

    public void setContents(ShakespeareContents contents) {
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShakespeareResponse that = (ShakespeareResponse) o;
        return Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }

    @Override
    public String toString() {
        return "ShakespeareResponse{" +
                "contents=" + contents +
                '}';
    }
}
