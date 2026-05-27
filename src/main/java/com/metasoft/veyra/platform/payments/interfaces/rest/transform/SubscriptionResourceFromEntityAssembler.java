package com.metasoft.veyra.platform.payments.interfaces.rest.transform;

import com.metasoft.veyra.platform.payments.domain.model.aggregates.Subscription;
import com.metasoft.veyra.platform.payments.interfaces.rest.resources.SubscriptionResource;

public class SubscriptionResourceFromEntityAssembler {
    public static SubscriptionResource toResourceFromEntity(Subscription entity) {
        return new SubscriptionResource(
                entity.getId(),
                entity.getUserId().userId(),
                entity.getStripeSubscriptionId(),
                entity.getPlanType().name(),
                entity.getPeriod().name(),
                entity.getAmount().value().doubleValue(),
                entity.getAmount().currency(),
                entity.getStatus().name(),
                entity.getCurrentPeriodStart(),
                entity.getCurrentPeriodEnd(),
                entity.getCreatedAt()
        );
    }
}