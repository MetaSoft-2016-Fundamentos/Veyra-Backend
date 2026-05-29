package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record AssignedRelativeForResidentResource(@NotNull(message = "Resident is required") Long residentId) {
}
