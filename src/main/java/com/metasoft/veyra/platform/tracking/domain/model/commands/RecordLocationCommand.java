package com.metasoft.veyra.platform.tracking.domain.model.commands;

public record RecordLocationCommand(String deviceId, double latitude, double longitude) {
}
