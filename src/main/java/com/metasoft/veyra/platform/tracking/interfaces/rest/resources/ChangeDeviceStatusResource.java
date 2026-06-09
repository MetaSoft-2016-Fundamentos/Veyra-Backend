package com.metasoft.veyra.platform.tracking.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record ChangeDeviceStatusResource(@NotBlank String status) {
}
