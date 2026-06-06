package com.metasoft.veyra.platform.payments.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public final class PaymentFailedEvent extends ApplicationEvent {
    private final Long paymentId;
    private final Long subscriptionId;
    private final String failureMessage;

    public PaymentFailedEvent(Object source, Long paymentId, Long subscriptionId, String failureMessage) {
        super(source);
        this.paymentId = paymentId;
        this.subscriptionId = subscriptionId;
        this.failureMessage = failureMessage;
    }
}