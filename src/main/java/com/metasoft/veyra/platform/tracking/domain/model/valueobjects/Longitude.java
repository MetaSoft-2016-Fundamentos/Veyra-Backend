package com.metasoft.veyra.platform.tracking.domain.model.valueobjects;

public record Longitude(double value) {
    public Longitude {
        if (value < -180 || value > 180) throw new IllegalArgumentException("Longitude must be between -180 and 180");
    }
}
