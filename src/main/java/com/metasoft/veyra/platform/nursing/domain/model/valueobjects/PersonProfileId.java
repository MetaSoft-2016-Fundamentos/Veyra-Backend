package com.metasoft.veyra.platform.nursing.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record PersonProfileId(Long personProfileId) {
public PersonProfileId{
    if (personProfileId == null) {
        throw new IllegalArgumentException("personProfileId cannot be null");
    }

}
}
