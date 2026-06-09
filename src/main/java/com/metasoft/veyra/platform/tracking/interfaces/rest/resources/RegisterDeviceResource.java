package com.metasoft.veyra.platform.tracking.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record RegisterDeviceResource(@NotBlank String deviceType, @NotBlank String macAddress) {
}
