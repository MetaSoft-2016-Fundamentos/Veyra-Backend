package com.metasoft.veyra.platform.tracking.domain.model.valueobjects;


public record RespiratoryRate(Integer value) {

    public RespiratoryRate {
        if (value == null || value < 0 || value > 60) {
            throw new IllegalArgumentException("Respiratory rate must be between 0 and 60 breaths/min");
        }
    }
}