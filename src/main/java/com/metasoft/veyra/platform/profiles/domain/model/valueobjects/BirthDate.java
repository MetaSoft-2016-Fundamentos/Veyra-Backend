package com.metasoft.veyra.platform.profiles.domain.model.valueobjects;

import java.time.LocalDate;

public record BirthDate(LocalDate birthDate) {
    public BirthDate {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }

        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
    }
}
