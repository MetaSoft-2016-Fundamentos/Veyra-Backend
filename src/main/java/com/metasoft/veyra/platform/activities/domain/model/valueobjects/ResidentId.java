package com.metasoft.veyra.platform.activities.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ResidentId(Long residentId) {
    public ResidentId {
        if (residentId == null || residentId <= 0) {
            throw new IllegalArgumentException("ResidentId must be a positive number.");
        }
    }
}