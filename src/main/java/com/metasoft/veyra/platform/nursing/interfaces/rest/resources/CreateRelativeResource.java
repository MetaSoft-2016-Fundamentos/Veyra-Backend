package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRelativeResource(
        @NotBlank(message = "First name should not be blank") String firstName,
        @NotBlank(message = "Last name should not be blank") String lastName,
        @Email(message = "Email should be valid") String email,
       @NotNull(message = "ResidentId is required") Long residentId

) {
}
