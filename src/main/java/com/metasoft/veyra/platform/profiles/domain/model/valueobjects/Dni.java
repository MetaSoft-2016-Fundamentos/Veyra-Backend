package com.metasoft.veyra.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Dni(String dni) {
    public Dni {
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("DNI cannot be null or blank");
        }
        if (!dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("DNI must be 8 digits");
        }
    }
}
