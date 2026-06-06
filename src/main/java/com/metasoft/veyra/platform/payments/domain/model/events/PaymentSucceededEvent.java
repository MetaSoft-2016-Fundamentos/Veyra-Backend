package com.metasoft.veyra.platform.payments.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

@Getter
public final class PaymentSucceededEvent extends ApplicationEvent {
    private final Long paymentId;
    private final Long subscriptionId;
    private final BigDecimal amount;

    public PaymentSucceededEvent(Object source, Long paymentId, Long subscriptionId, BigDecimal amount) {
        super(source);
        this.paymentId = paymentId;
        this.subscriptionId = subscriptionId;
        this.amount = amount;
    }
    }