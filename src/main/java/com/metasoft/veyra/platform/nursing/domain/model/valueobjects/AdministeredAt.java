package com.metasoft.veyra.platform.nursing.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Embeddable
public record AdministeredAt(LocalDateTime administeredAt) {

    public AdministeredAt {
        if (administeredAt == null) {
            throw new IllegalArgumentException("Administered date/time cannot be null");
        }
        LocalDateTime now = LocalDateTime.now();
        if (administeredAt.isAfter(now)) {
            throw new IllegalArgumentException(
                    "Administered date/time cannot be in the future");
        }
        long hoursAgo = ChronoUnit.HOURS.between(administeredAt, now);
        if (hoursAgo > 72) {
            throw new IllegalArgumentException(
                    "Administered date/time cannot be more than 72 hours in the past. " +
                            "Found: " + hoursAgo + " hours ago");
        }
        if (administeredAt.getYear() < 2025) {
            throw new IllegalArgumentException(
                    "Administered date/time is invalid: " + administeredAt);
        }
    }
    public static AdministeredAt now() {
        return new AdministeredAt(LocalDateTime.now());
    }
}