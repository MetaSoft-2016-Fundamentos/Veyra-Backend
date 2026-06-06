package com.metasoft.veyra.platform.payments.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record Amount(
        BigDecimal value,
        String currency
) {
    public Amount {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
    }

    public Amount(double value, String currency) {
        this(BigDecimal.valueOf(value), currency);
    }

    public static Amount usd(double value) {
        return new Amount(value, "USD");
    }

    public Amount multiply(double multiplier) {
        return new Amount(value.multiply(BigDecimal.valueOf(multiplier)), currency);
    }
}