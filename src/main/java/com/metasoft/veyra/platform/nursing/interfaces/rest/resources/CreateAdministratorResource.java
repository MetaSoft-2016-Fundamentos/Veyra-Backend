package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;


public record CreateAdministratorResource(@NotBlank(message = "username is required")
                                                  String username ,@NotBlank(message = "password is required") String password) {
}
