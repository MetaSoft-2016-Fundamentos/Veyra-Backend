package com.metasoft.veyra.platform.profiles.domain.model.valueobjects;

public record PhoneNumber(String phoneNumber) {
    public PhoneNumber {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }
        // Basic validation: check if it contains only digits, spaces, dashes, parentheses, or plus sign
        if (!phoneNumber.matches("[+\\d()\\s-]+")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }
}
