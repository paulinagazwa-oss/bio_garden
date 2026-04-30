package com.github.paulinagazwa.oss.bio.garden.exception;

public class WeatherServiceException extends RuntimeException {

	public WeatherServiceException(String message) {
		super(message);
	}

	public WeatherServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
