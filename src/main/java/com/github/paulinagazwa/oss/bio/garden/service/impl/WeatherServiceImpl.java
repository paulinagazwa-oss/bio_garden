package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.exception.WeatherServiceException;
import com.github.paulinagazwa.oss.bio.garden.model.WeatherResponse;
import com.github.paulinagazwa.oss.bio.garden.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

	public static final String WEATHER_PROPERTY = "temperature_2m,apparent_temperature,relative_humidity_2m,precipitation,rain,snowfall,weather_code,wind_speed_10m,wind_direction_10m,uv_index,is_day";

	private final RestClient restClient;

	// TODO location should be configurable, user should be able to set it via API or set it in profile
	@Value("${garden.weather.latitude}")
	private double latitude;

	@Value("${garden.weather.longitude}")
	private double longitude;

	@Override
	public Optional<WeatherResponse> getCurrentWeather() {

		try {
			return Optional.ofNullable(
					restClient.get()
							.uri(uriBuilder -> uriBuilder
									.queryParam("latitude", latitude)
									.queryParam("longitude", longitude)
									.queryParam("current", WEATHER_PROPERTY)
									.build())
							.retrieve()
							.onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
								throw new WeatherServiceException("Weather service client error: " + response.getStatusCode());
							})
							.onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
								throw new WeatherServiceException("Weather service unavailable: " + response.getStatusCode());
							})
							.body(WeatherResponse.class)
			);
		} catch (RestClientException e) {
			throw new WeatherServiceException("Failed to connect to weather service: " + e.getMessage(), e);
		}
	}

}
