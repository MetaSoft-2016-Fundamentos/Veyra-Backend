package com.metasoft.veyra.platform.tracking.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AssignDeviceResource(        @NotNull(message = "Resident ID must not be null")
                                           Long residentId,
                                           @NotBlank(message = "Assigned by must not be blank")
                                           @Size(max = 100, message = "Assigned by must not exceed 100 characters")String assignedBy) {
}
