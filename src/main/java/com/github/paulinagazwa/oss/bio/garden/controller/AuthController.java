package com.github.paulinagazwa.oss.bio.garden.controller;

import com.github.paulinagazwa.oss.bio.garden.entity.UserEntity;
import com.github.paulinagazwa.oss.bio.garden.model.RegisterUserRequest;
import com.github.paulinagazwa.oss.bio.garden.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
// TODO versioning?
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

	// TODO openApi
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserRequest request) {

        UserEntity user = userService.register(
                request.email(),
                request.username(),
                request.password(),
                request.latitude(),
                request.longitude()
        );

        return ResponseEntity.ok("User created with id: " + user.getId());
    }
}
