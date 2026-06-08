package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record AssignedRoomForResidentResource(@NotBlank(message = "Room is required") String roomNumber) {
}
