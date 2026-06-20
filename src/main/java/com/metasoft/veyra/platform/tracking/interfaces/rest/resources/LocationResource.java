package com.metasoft.veyra.platform.tracking.interfaces.rest.resources;

public record LocationResource(String id, Long deviceId, double latitude, double longitude, String recordedAt) {
}
