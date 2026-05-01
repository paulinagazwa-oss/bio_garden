package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.exception.WeatherServiceException;
import com.github.paulinagazwa.oss.bio.garden.logging.LogMessages;
import com.github.paulinagazwa.oss.bio.garden.model.WeatherResponse;
import com.github.paulinagazwa.oss.bio.garden.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
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

		log.debug(LogMessages.WEATHER_GET_CURRENT_START, latitude, longitude);

		try {
			WeatherResponse response = restClient.get()
					.uri(uriBuilder -> uriBuilder
							.queryParam("latitude", latitude)
							.queryParam("longitude", longitude)
							.queryParam("current", WEATHER_PROPERTY)
							.build())
					.retrieve()
					.onStatus(HttpStatusCode::is4xxClientError, (request, httpResponse) -> {
						log.warn(LogMessages.WEATHER_CLIENT_ERROR, httpResponse.getStatusCode());
						throw new WeatherServiceException("Weather service client error: " + httpResponse.getStatusCode());
					})
					.onStatus(HttpStatusCode::is5xxServerError, (request, httpResponse) -> {
						log.error(LogMessages.WEATHER_SERVER_ERROR, httpResponse.getStatusCode());
						throw new WeatherServiceException("Weather service unavailable: " + httpResponse.getStatusCode());
					})
					.body(WeatherResponse.class);

			if (response == null) {
				log.debug(LogMessages.WEATHER_GET_CURRENT_EMPTY);
				return Optional.empty();
			}

			log.debug(LogMessages.WEATHER_GET_CURRENT_SUCCESS);
			return Optional.of(response);
		} catch (RestClientException e) {
			log.error(LogMessages.WEATHER_CONNECTION_ERROR, e);
			throw new WeatherServiceException("Failed to connect to weather service: " + e.getMessage(), e);
		}
	}

}
