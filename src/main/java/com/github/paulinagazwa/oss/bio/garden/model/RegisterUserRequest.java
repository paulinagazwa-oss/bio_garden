package com.github.paulinagazwa.oss.bio.garden.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

// TODO move to openApi specification
public record RegisterUserRequest(
        @Size(max = 320)
        String email,

        @Size(max = 100)
        String username,

        @Size(min = 8, max = 100)
        String password,

        @DecimalMin(value = "-90.0")
        @DecimalMax(value = "90.0")
        Double latitude,

        @DecimalMin(value = "-180.0")
        @DecimalMax(value = "180.0")
        Double longitude
) {
}
