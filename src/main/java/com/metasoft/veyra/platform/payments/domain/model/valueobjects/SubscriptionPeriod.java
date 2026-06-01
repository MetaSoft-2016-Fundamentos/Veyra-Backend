package com.metasoft.veyra.platform.payments.domain.model.valueobjects;

import lombok.Getter;

@Getter
public enum SubscriptionPeriod {
    MONTHLY("month", 1),
    ANNUALLY("year", 12);

    private final String stripeInterval;
    private final int months;

    SubscriptionPeriod(String stripeInterval, int months) {
        this.stripeInterval = stripeInterval;
        this.months = months;
    }

}