package com.metasoft.veyra.platform.tracking.domain.exceptions;

public class DeviceAlreadyExistsException extends RuntimeException {
    public DeviceAlreadyExistsException(String message) {
        super(message);
    }
}
