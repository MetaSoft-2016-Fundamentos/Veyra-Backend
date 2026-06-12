package com.metasoft.veyra.platform.tracking.domain.model.aggregates;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "gps_locations")
public class GpsLocation {

    @Id
    private String id;
    private String deviceId;
    private double latitude;
    private double longitude;
    private LocalDateTime recordedAt;

    public GpsLocation(String deviceId, double latitude, double longitude) {
        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.recordedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
}
