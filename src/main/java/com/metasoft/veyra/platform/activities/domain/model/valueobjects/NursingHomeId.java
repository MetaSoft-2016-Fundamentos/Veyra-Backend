package com.metasoft.veyra.platform.activities.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record NursingHomeId(Long nursingHomeId) {
    public NursingHomeId {
        if (nursingHomeId == null || nursingHomeId <= 0) {
            throw new IllegalArgumentException("NursingHomeId must be a positive number.");
        }
    }
}