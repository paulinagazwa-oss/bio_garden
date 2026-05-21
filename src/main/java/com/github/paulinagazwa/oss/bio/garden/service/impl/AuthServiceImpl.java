package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.logging.LogMessages;
import com.github.paulinagazwa.oss.bio.garden.model.AuthTokenResponse;
import com.github.paulinagazwa.oss.bio.garden.security.CustomUserDetailsService;
import com.github.paulinagazwa.oss.bio.garden.security.JwtService;
import com.github.paulinagazwa.oss.bio.garden.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;

	private final CustomUserDetailsService userDetailsService;

	private final JwtService jwtService;

	@Value("${security.jwt.access-token-expiration}")
	private long accessTokenExpiration;

	@Override
	public AuthTokenResponse login(String login, String password) {

		log.debug(LogMessages.AUTH_LOGIN_START, login);

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(login, password)
		);

		UserDetails userDetails = userDetailsService.loadUserByUsername(login);
		log.debug(LogMessages.AUTH_LOGIN_SUCCESS, login);
		return buildTokenResponse(userDetails);
	}

	@Override
	public AuthTokenResponse refresh(String refreshToken) {

		String username = jwtService.extractUsername(refreshToken);
		log.debug(LogMessages.AUTH_REFRESH_START, username);

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		if (!jwtService.isTokenValid(refreshToken, userDetails)) {
			log.warn(LogMessages.AUTH_REFRESH_INVALID_TOKEN, username);
			throw new IllegalArgumentException("Invalid or expired refresh token");
		}

		log.debug(LogMessages.AUTH_REFRESH_SUCCESS, username);
		return buildTokenResponse(userDetails);
	}

	private AuthTokenResponse buildTokenResponse(UserDetails userDetails) {

		String accessToken = jwtService.generateAccessToken(userDetails);
		String newRefreshToken = jwtService.generateRefreshToken(userDetails);
		int expiresIn = (int) (accessTokenExpiration / 1000);

		return new AuthTokenResponse()
				.accessToken(accessToken)
				.accessTokenType("Bearer")
				.expiresIn(expiresIn)
				.refreshToken(newRefreshToken);
	}
}
