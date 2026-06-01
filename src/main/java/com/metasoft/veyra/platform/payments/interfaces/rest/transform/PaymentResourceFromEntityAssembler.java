package com.metasoft.veyra.platform.payments.interfaces.rest.transform;

import com.metasoft.veyra.platform.payments.domain.model.aggregates.Payment;
import com.metasoft.veyra.platform.payments.interfaces.rest.resources.PaymentResource;

public class PaymentResourceFromEntityAssembler {
    public static PaymentResource toResourceFromEntity(Payment entity) {
        return new PaymentResource(
                entity.getId(),
                entity.getSubscription().getId(),
                entity.getStripePaymentIntentId(),
                entity.getAmount() != null ? entity.getAmount().value().doubleValue() : null,
                entity.getAmount() != null ? entity.getAmount().currency() : null,
                entity.getStatus() != null ? entity.getStatus().name() : null,
                entity.getStripeReceiptUrl()
        );
    }
}
