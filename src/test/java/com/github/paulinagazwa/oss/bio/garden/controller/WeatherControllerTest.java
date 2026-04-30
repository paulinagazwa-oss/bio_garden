package com.github.paulinagazwa.oss.bio.garden.controller;

import com.github.paulinagazwa.oss.bio.garden.model.CurrentWeather;
import com.github.paulinagazwa.oss.bio.garden.model.WeatherResponse;
import com.github.paulinagazwa.oss.bio.garden.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

	@Mock
	private WeatherService weatherService;

	@InjectMocks
	private WeatherController weatherController;

	@Test
	void shouldReturnCurrentWeatherWhenServiceProvidesData() {

		WeatherResponse weatherResponse = new WeatherResponse(
				new CurrentWeather(21.5, 20.0, 60, 0.0, 0.0, 0.0, 1, 12.3, 180, 5.2, 1)
		);
		when(weatherService.getCurrentWeather()).thenReturn(Optional.of(weatherResponse));

		ResponseEntity<WeatherResponse> response = weatherController.getCurrentWeather();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(weatherResponse, response.getBody());
		verify(weatherService).getCurrentWeather();
	}

	@Test
	void shouldReturnNotFoundWhenServiceProvidesNoData() {

		when(weatherService.getCurrentWeather()).thenReturn(Optional.empty());

		ResponseEntity<WeatherResponse> response = weatherController.getCurrentWeather();

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
		verify(weatherService).getCurrentWeather();
	}

}
