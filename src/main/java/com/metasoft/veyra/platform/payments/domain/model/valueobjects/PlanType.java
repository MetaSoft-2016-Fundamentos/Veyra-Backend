package com.metasoft.veyra.platform.payments.domain.model.valueobjects;

import lombok.Getter;

@Getter
public enum PlanType {
    FAMILY("Family Plan", 30.0, 300.0),
    NURSING_HOME("Nursing Home Plan", 300.0, 3000.0);

    private final String displayName;
    private final double monthlyPrice;
    private final double annualPrice;

    PlanType(String displayName, double monthlyPrice, double annualPrice) {
        this.displayName = displayName;
        this.monthlyPrice = monthlyPrice;
        this.annualPrice = annualPrice;
    }

    public Amount getPrice(SubscriptionPeriod period) {
        double price = period == SubscriptionPeriod.MONTHLY ? monthlyPrice : annualPrice;
        return Amount.usd(price);
    }
}
