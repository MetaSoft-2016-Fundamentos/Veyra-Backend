package com.metasoft.veyra.platform.tracking.domain.model.valueobjects;


public record DeviceId(String deviceId) {

    public DeviceId {
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("Device ID cannot be null or empty");
        }
    }
}