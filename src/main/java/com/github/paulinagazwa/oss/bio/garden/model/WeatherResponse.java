package com.github.paulinagazwa.oss.bio.garden.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherResponse(@JsonProperty("current") CurrentWeather current) {

}
