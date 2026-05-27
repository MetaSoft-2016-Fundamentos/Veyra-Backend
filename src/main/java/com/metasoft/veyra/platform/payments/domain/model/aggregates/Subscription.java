package com.metasoft.veyra.platform.payments.domain.model.aggregates;

import com.metasoft.veyra.platform.payments.domain.model.valueobjects.*;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;


@Entity
@Getter
public class Subscription extends AuditableAbstractAggregateRoot<Subscription> {
    @Embedded
    private UserId userId;

    @Column(unique = true, nullable = false)
    private String stripeSubscriptionId;

    @Column(nullable = false)
    private String stripeCustomerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanType planType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionPeriod period;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "amount_value")),
            @AttributeOverride(name = "currency", column = @Column(name = "amount_currency"))
    })
    private Amount amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    private LocalDateTime currentPeriodStart;
    private LocalDateTime currentPeriodEnd;
    private LocalDateTime canceledAt;

    protected Subscription() {
    }

    public Subscription(UserId userId, String stripeSubscriptionId, String stripeCustomerId,
                        PlanType planType, SubscriptionPeriod period) {
        this.userId = userId;
        this.stripeSubscriptionId = stripeSubscriptionId;
        this.stripeCustomerId = stripeCustomerId;
        this.planType = planType;
        this.period = period;
        this.amount = planType.getPrice(period);
        this.status = SubscriptionStatus.INCOMPLETE;
    }

    public void activate(LocalDateTime periodStart, LocalDateTime periodEnd) {
        this.status = SubscriptionStatus.ACTIVE;
        this.currentPeriodStart = periodStart;
        this.currentPeriodEnd = periodEnd;
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }

    public void updateStatus(SubscriptionStatus newStatus) {
        this.status = newStatus;
    }

    public void updatePeriod(LocalDateTime start, LocalDateTime end) {
        this.currentPeriodStart = start;
        this.currentPeriodEnd = end;
    }

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE;
    }
}