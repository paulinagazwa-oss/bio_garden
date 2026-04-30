package com.github.paulinagazwa.oss.bio.garden.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class WeatherClientConfig {

	@Value("${garden.weather.api.url}")
	private String apiUrl;

	@Bean
	public RestClient weatherRestClient() {

		return RestClient.builder()
				.baseUrl(apiUrl)
				.build();
	}

}
