package com.github.paulinagazwa.oss.bio.garden.service;

import com.github.paulinagazwa.oss.bio.garden.entity.UserEntity;

public interface UserService {

	UserEntity register(String email, String username, String rawPassword, Double latitude, Double longitude);
}
