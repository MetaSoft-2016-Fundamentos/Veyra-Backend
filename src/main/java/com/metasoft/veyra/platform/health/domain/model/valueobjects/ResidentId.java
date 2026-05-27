package com.metasoft.veyra.platform.health.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ResidentId(Long residentId) {
    public ResidentId{
        if (residentId==null){
            throw new IllegalArgumentException("Resident id cannot be null");
        }
        if (residentId<1){
            throw new IllegalArgumentException("Resident id cannot be less than 1");
        }
    }
}
