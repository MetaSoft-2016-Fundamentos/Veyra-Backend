package com.metasoft.veyra.platform.nursing.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(Long userId) {
    public UserId{
        if (userId == null|| userId<1) {
            throw new NullPointerException("userId cannot be null or then less 1");
        }
    }
}
