package com.github.paulinagazwa.oss.bio.garden.service;

import com.github.paulinagazwa.oss.bio.garden.model.WeatherResponse;

import java.util.Optional;

public interface WeatherService {

	Optional<WeatherResponse> getCurrentWeather();

}
