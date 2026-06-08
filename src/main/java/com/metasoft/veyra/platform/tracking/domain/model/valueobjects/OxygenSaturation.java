package com.metasoft.veyra.platform.tracking.domain.model.valueobjects;



public record OxygenSaturation(Integer value) {

    public OxygenSaturation {
        if (value == null || value < 0 || value > 100) {
            throw new IllegalArgumentException("Oxygen saturation must be between 0 and 100%");
        }
    }
}