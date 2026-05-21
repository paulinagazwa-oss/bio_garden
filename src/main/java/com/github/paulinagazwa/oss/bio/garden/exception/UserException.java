package com.github.paulinagazwa.oss.bio.garden.exception;

public class UserException extends RuntimeException {

	private UserException(String message) {

		super(message);
	}

	public static UserException loginRequired() {

		return new UserException("Login must be provided");
	}

	public static UserException alreadyExists(String field) {

		return new UserException(field + " already exists");
	}
}
