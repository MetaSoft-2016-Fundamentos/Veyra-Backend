package com.metasoft.veyra.platform.health.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record MeasurementId(String  measurementId) {
public MeasurementId{
    if (measurementId==null|| measurementId.isEmpty()){
        throw new IllegalArgumentException("measurement id cannot be null o then less 1");
    }
}
}
