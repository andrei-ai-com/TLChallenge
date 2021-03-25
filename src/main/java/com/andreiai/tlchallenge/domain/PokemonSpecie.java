package com.andreiai.tlchallenge.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonSpecie {

    @JsonProperty("flavor_text_entries")
    private List<FlavourTextEntry> textEntries;

    public PokemonSpecie() {
    }

    public PokemonSpecie(List<FlavourTextEntry> textEntries) {
        this.textEntries = textEntries;
    }

    public List<FlavourTextEntry> getTextEntries() {
        return textEntries;
    }

    public void setTextEntries(List<FlavourTextEntry> textEntries) {
        this.textEntries = textEntries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PokemonSpecie that = (PokemonSpecie) o;
        return Objects.equals(textEntries, that.textEntries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textEntries);
    }

    @Override
    public String toString() {
        return "PokemonSpecie{" +
                "textEntries=" + textEntries +
                '}';
    }
}
