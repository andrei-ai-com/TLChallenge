package com.andreiai.tlchallenge.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlavourTextEntryLanguage {

    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;

    public FlavourTextEntryLanguage() {
    }

    public FlavourTextEntryLanguage(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlavourTextEntryLanguage that = (FlavourTextEntryLanguage) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }

    @Override
    public String toString() {
        return "FlavourTextEntryLanguage{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
