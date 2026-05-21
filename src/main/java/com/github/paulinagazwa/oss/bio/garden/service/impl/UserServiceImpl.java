package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.UserEntity;
import com.github.paulinagazwa.oss.bio.garden.exception.UserException;
import com.github.paulinagazwa.oss.bio.garden.model.RegisterRequest;
import com.github.paulinagazwa.oss.bio.garden.repository.UserRepository;
import com.github.paulinagazwa.oss.bio.garden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserEntity register(RegisterRequest request) {

		validateData(request.getEmail(), request.getLogin());

		UserEntity user = new UserEntity();
		user.setEmail(request.getEmail());
		user.setUsername(request.getLogin());
		user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
		user.setLatitude(request.getLatitude());
		user.setLongitude(request.getLongitude());
		user.setEnabled(true);
		user.setNotificationsEnabled(request.getNotificationsEnabled() != null ? request.getNotificationsEnabled() : true);
		user.setCreationDate(LocalDateTime.now());
		user.setLastUpdateDate(LocalDateTime.now());

		return userRepository.save(user);
	}

	private void validateData(String email, String username) {

		if ((email == null || email.isBlank()) && (username == null || username.isBlank())) {
			throw UserException.loginRequired();
		}

		if (email != null && !email.isBlank() && userRepository.existsByEmailIgnoreCase(email)) {
			throw UserException.alreadyExists("Email");
		}

		if (username != null && !username.isBlank() && userRepository.existsByUsernameIgnoreCase(username)) {
			throw UserException.alreadyExists("Username");
		}
	}
}
