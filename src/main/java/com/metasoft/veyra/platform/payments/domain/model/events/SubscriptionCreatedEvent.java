package com.metasoft.veyra.platform.payments.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public final class SubscriptionCreatedEvent extends ApplicationEvent {
    private final Long subscriptionId;
    private final Long userId;
    private final String stripeSubscriptionId;

    public SubscriptionCreatedEvent(Object source, Long subscriptionId, Long userId, String stripeSubscriptionId) {
        super(source);
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.stripeSubscriptionId = stripeSubscriptionId;
    }
}