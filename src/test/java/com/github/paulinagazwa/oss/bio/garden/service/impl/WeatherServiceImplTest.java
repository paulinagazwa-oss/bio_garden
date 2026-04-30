package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.exception.WeatherServiceException;
import com.github.paulinagazwa.oss.bio.garden.model.CurrentWeather;
import com.github.paulinagazwa.oss.bio.garden.model.WeatherResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;
import java.util.function.Function;

import static com.github.paulinagazwa.oss.bio.garden.service.impl.WeatherServiceImpl.WEATHER_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

	@Mock
	private RestClient restClient;

	@Mock
	private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

	@Mock
	private RestClient.ResponseSpec responseSpec;

	@InjectMocks
	private WeatherServiceImpl weatherService;

	@Test
	void getCurrentWeather_returnsWeatherResponse_whenServiceRespondsSuccessfully() {

		WeatherResponse expectedResponse = new WeatherResponse(new CurrentWeather(
				20.0, 18.0, 60, 0.0, 0.0, 0.0, 1, 10.0, 180, 3.0, 1
		));

		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(WeatherResponse.class)).thenReturn(expectedResponse);

		Optional<WeatherResponse> result = weatherService.getCurrentWeather();

		assertThat(result).isPresent().contains(expectedResponse);
	}

	@Test
	void getCurrentWeather_returnsEmptyOptional_whenServiceReturnsNullBody() {

		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(WeatherResponse.class)).thenReturn(null);

		Optional<WeatherResponse> result = weatherService.getCurrentWeather();

		assertThat(result).isEmpty();
	}

	@Test
	void getCurrentWeather_throwsWeatherServiceException_whenRestClientExceptionOccurs() {

		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(WeatherResponse.class)).thenThrow(new RestClientException("connection refused"));

		assertThatThrownBy(() -> weatherService.getCurrentWeather())
				.isInstanceOf(WeatherServiceException.class)
				.hasMessageContaining("Failed to connect to weather service");
	}

	@Test
	void getCurrentWeather_preservesCause_whenRestClientExceptionOccurs() {

		RestClientException cause = new RestClientException("timeout");

		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(WeatherResponse.class)).thenThrow(cause);

		assertThatThrownBy(() -> weatherService.getCurrentWeather())
				.isInstanceOf(WeatherServiceException.class)
				.hasCause(cause);
	}

	@Test
	void getCurrentWeather_throwsWeatherServiceException_whenClientErrorOccurs() {

		WeatherServiceException clientError = new WeatherServiceException("Weather service client error: 400 BAD_REQUEST");

		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(WeatherResponse.class)).thenThrow(clientError);

		assertThatThrownBy(() -> weatherService.getCurrentWeather())
				.isInstanceOf(WeatherServiceException.class)
				.hasMessageContaining("Weather service client error");
	}

	@Test
	void getCurrentWeather_buildsUriWithLatitudeLongitudeAndCurrentParameters() {

		org.springframework.web.util.UriBuilder uriBuilder = org.mockito.Mockito.mock(org.springframework.web.util.UriBuilder.class);
		java.net.URI expectedUri = java.net.URI.create("https://example.com?latitude=1.23&longitude=4.56&current=" + WEATHER_PROPERTY);

		org.springframework.test.util.ReflectionTestUtils.setField(weatherService, "latitude", 1.23d);
		org.springframework.test.util.ReflectionTestUtils.setField(weatherService, "longitude", 4.56d);

		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(any(Function.class))).thenAnswer(invocation -> {
			Function<org.springframework.web.util.UriBuilder, java.net.URI> uriFunction = invocation.getArgument(0);
			uriFunction.apply(uriBuilder);
			return requestHeadersUriSpec;
		});
		when(uriBuilder.queryParam("latitude", 1.23d)).thenReturn(uriBuilder);
		when(uriBuilder.queryParam("longitude", 4.56d)).thenReturn(uriBuilder);
		when(uriBuilder.queryParam("current", WEATHER_PROPERTY)).thenReturn(uriBuilder);
		when(uriBuilder.build()).thenReturn(expectedUri);
		when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(WeatherResponse.class)).thenReturn(null);

		weatherService.getCurrentWeather();

		verify(uriBuilder).queryParam("latitude", 1.23d);
		verify(uriBuilder).queryParam("longitude", 4.56d);
		verify(uriBuilder).queryParam("current", WEATHER_PROPERTY);
		verify(uriBuilder).build();
	}
}
