package com.github.paulinagazwa.oss.bio.garden.service;

import com.github.paulinagazwa.oss.bio.garden.entity.UserEntity;
import com.github.paulinagazwa.oss.bio.garden.model.RegisterRequest;

public interface UserService {

	UserEntity register(RegisterRequest request);
}
