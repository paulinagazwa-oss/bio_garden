package com.github.paulinagazwa.oss.bio.garden.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CurrentWeather(
		@JsonProperty("temperature_2m") Double temperature,
		@JsonProperty("apparent_temperature") Double apparentTemperature,
		@JsonProperty("relative_humidity_2m") Integer relativeHumidity,
		@JsonProperty("precipitation") Double precipitation,
		@JsonProperty("rain") Double rain,
		@JsonProperty("snowfall") Double snowfall,
		@JsonProperty("weather_code") Integer weatherCode,
		@JsonProperty("wind_speed_10m") Double windSpeed,
		@JsonProperty("wind_direction_10m") Integer windDirection,
		@JsonProperty("uv_index") Double uvIndex,
		@JsonProperty("is_day") Integer isDay
) {}
