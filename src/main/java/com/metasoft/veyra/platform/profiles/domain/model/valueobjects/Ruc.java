package com.metasoft.veyra.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Ruc(String ruc ) {
    public Ruc {
        if (ruc == null || !ruc.matches("\\d{11}")) {
            throw new IllegalArgumentException("RUC must be 11 digits");
        }
    }
}
