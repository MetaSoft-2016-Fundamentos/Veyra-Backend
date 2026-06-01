package com.metasoft.veyra.platform.tracking.domain.model.valueobjects;



public record Temperature(Double value) {

    public Temperature {
        if (value == null || value < 30.0 || value > 45.0) {
            throw new IllegalArgumentException("Temperature must be between 30.0 and 45.0 °C");
        }
    }
}