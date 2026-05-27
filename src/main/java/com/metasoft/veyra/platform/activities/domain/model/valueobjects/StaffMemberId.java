package com.metasoft.veyra.platform.activities.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record StaffMemberId(Long staffMemberId) {
    public StaffMemberId {
        if (staffMemberId == null || staffMemberId <= 0) {
            throw new IllegalArgumentException("StaffMemberId must be a positive number.");
        }
    }
}