package com.metasoft.veyra.platform.tracking.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record RecordLocationResource(@NotBlank String deviceId, double latitude, double longitude) {
}
