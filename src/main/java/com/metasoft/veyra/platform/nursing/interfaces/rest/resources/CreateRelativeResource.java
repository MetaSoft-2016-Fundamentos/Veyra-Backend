package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record CreateRelativeResource(@NotBlank(message = "Username is required") String username,
                                     @NotBlank(message = "Password is required") String password) {
}
