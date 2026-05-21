package com.github.paulinagazwa.oss.bio.garden.service;

import com.github.paulinagazwa.oss.bio.garden.model.AuthTokenResponse;

public interface AuthService {

	AuthTokenResponse login(String login, String password);

	AuthTokenResponse refresh(String refreshToken);
}
