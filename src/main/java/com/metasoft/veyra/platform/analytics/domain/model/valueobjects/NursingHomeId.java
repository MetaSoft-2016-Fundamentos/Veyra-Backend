package com.metasoft.veyra.platform.analytics.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record NursingHomeId(Long nursingHomeId) {
    public NursingHomeId{
        if (nursingHomeId==null){
            throw new IllegalArgumentException("nursing home id cannot be null");
        }
        if (nursingHomeId<1){
            throw new IllegalArgumentException("nursing home id cannot be less than 1");
        }
    }

}
