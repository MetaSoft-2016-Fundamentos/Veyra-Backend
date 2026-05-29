package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateRelativeResource(
        @Email(message = "Email should be valid") String email,
        @NotBlank(message = "First name should not be blank") String firstName,
        @NotBlank(message = "Last name should not be blank") String lastName
) {
}
