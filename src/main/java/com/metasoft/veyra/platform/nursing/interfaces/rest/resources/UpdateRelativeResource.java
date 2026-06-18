package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateRelativeResource(@NotBlank String firstName, @NotBlank String lastName, @Email String email, @NotBlank Long residentId) {
}
