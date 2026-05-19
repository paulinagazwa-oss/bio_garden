package com.github.paulinagazwa.oss.bio.garden.controller;

import com.github.paulinagazwa.oss.bio.garden.entity.UserEntity;
import com.github.paulinagazwa.oss.bio.garden.model.AuthTokenResponse;
import com.github.paulinagazwa.oss.bio.garden.model.LoginRequest;
import com.github.paulinagazwa.oss.bio.garden.model.RefreshTokenRequest;
import com.github.paulinagazwa.oss.bio.garden.model.RegisterRequest;
import com.github.paulinagazwa.oss.bio.garden.service.AuthService;
import com.github.paulinagazwa.oss.bio.garden.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;
	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {

		UserEntity user = userService.register(
				request.getEmail(),
				request.getLogin(),
				request.getPassword(),
				request.getLatitude(),
				request.getLongitude()
		);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("message", "User created with login: " + user.getUsername()));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthTokenResponse> login(@Valid @RequestBody LoginRequest request) {

		AuthTokenResponse response = authService.login(request.getLogin(), request.getPassword());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {

		AuthTokenResponse response = authService.refresh(request.getRefreshToken());
		return ResponseEntity.ok(response);
	}
}
