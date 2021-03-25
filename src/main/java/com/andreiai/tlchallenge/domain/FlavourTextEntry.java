package com.andreiai.tlchallenge.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlavourTextEntry {

    @JsonProperty("flavor_text")
    private String text;
    @JsonProperty("language")
    private FlavourTextEntryLanguage language;

    public FlavourTextEntry() {
    }

    public FlavourTextEntry(String text, FlavourTextEntryLanguage language) {
        this.text = text;
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FlavourTextEntryLanguage getLanguage() {
        return language;
    }

    public void setLanguage(FlavourTextEntryLanguage language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlavourTextEntry that = (FlavourTextEntry) o;
        return Objects.equals(text, that.text) &&
                Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, language);
    }

    @Override
    public String toString() {
        return "FlavourTextEntry{" +
                "text='" + text + '\'' +
                ", language=" + language +
                '}';
    }
}
