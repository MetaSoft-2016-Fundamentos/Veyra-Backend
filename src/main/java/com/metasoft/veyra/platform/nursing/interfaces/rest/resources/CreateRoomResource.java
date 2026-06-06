package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateRoomResource(
       @NotNull(message = "Capacity is required")
       @Positive(message = "Capacity must be greater than 0")
       Integer capacity,
        @NotBlank(message = "Type is required") String type, @NotBlank(message = "Room number is required") String roomNumber) {
}
