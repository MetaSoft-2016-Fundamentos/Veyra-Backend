package com.metasoft.veyra.platform.tracking.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;


public record AssignDeviceResource(@NotNull(message = "Resident ID must not be null")Long residentId) {
}
