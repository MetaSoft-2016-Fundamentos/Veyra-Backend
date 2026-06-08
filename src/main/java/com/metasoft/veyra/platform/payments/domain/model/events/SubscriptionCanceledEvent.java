package com.metasoft.veyra.platform.payments.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public final class SubscriptionCanceledEvent extends ApplicationEvent {
    private final Long subscriptionId;
    private final Long userId;

    public SubscriptionCanceledEvent(Object source, Long subscriptionId, Long userId) {
        super(source);
        this.subscriptionId = subscriptionId;
        this.userId = userId;
    }
}