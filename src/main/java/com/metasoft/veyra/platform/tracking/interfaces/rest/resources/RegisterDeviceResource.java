package com.metasoft.veyra.platform.tracking.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record RegisterDeviceResource(@NotBlank(message = "Device is required") String deviceType, @NotBlank(message = "MacAddress is required") String macAddress) {
}
