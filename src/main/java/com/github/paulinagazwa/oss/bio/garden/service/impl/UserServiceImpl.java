package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.UserEntity;
import com.github.paulinagazwa.oss.bio.garden.exception.UserException;
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
	public UserEntity register(String email, String username, String rawPassword, Double latitude, Double longitude) {

		validateData(email, username);

		UserEntity user = new UserEntity();
		user.setEmail(email);
		user.setUsername(username);
		user.setPasswordHash(passwordEncoder.encode(rawPassword));
		user.setLatitude(latitude);
		user.setLongitude(longitude);
		user.setEnabled(true);
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
