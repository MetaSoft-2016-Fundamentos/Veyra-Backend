package com.metasoft.veyra.platform.payments.domain.model.aggregates;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.Amount;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.PaymentStatus;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.UserId;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
@Entity
@Getter
public class Payment extends AuditableAbstractAggregateRoot<Payment> {

    @JoinColumn(name = "subscription_id")
    @ManyToOne()
    private Subscription subscription;
@Embedded
    private UserId userId;

    @Column(unique = true, nullable = false)
    private String stripePaymentIntentId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "amount_value")),
            @AttributeOverride(name = "currency", column = @Column(name = "amount_currency"))
    })
    private Amount amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private String failureMessage;

    @Column(length = 1000)
    private String stripeReceiptUrl;

    protected Payment() {
    }

    public Payment(Subscription subscription, UserId userId, String stripePaymentIntentId, Amount amount) {
        this.subscription = subscription;
        this.userId = userId;
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    public void markAsSucceeded(String receiptUrl) {
        this.status = PaymentStatus.SUCCEEDED;
        this.stripeReceiptUrl = receiptUrl;
    }

    public void markAsFailed(String failureMessage) {
        this.status = PaymentStatus.FAILED;
        this.failureMessage = failureMessage;
    }

    public void markAsProcessing() {
        this.status = PaymentStatus.PROCESSING;
    }

    public void updateStatus(PaymentStatus newStatus) {
        this.status = newStatus;
    }
}