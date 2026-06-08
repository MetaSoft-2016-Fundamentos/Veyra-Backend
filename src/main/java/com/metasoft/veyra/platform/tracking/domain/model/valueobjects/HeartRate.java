package com.metasoft.veyra.platform.tracking.domain.model.valueobjects;


public record HeartRate(Integer value) {

    public HeartRate {
        if (value == null || value < 0 || value > 300) {
            throw new IllegalArgumentException("Heart rate must be between 0 and 300 bpm");
        }
    }
}