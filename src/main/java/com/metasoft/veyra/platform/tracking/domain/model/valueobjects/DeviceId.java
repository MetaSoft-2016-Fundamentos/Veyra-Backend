package com.metasoft.veyra.platform.tracking.domain.model.valueobjects;


public record DeviceId(Long deviceId) {

    public DeviceId {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID cannot be null or empty");
        }
    }
}