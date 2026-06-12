package com.metasoft.veyra.platform.tracking.domain.model.valueobjects;

public record Latitude(double value) {
    public Latitude {
        if (value < -90 || value > 90) throw new IllegalArgumentException("Latitude must be between -90 and 90");
    }
}
