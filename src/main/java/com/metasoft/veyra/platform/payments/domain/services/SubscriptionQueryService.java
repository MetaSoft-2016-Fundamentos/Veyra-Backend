package com.metasoft.veyra.platform.payments.domain.services;

import com.metasoft.veyra.platform.payments.domain.model.aggregates.Subscription;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetSubscriptionActiveByUserId;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetSubscriptionByIdQuery;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetSubscriptionByStripeSubscriptionIdQuery;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetSubscriptionByUserId;

import java.util.List;
import java.util.Optional;

public interface SubscriptionQueryService {
    Optional<Subscription>handle(GetSubscriptionByIdQuery query);
    Optional<Subscription>  handle(GetSubscriptionByStripeSubscriptionIdQuery query);
    List<Subscription> handle(GetSubscriptionByUserId query);
    Optional<Subscription>handle(GetSubscriptionActiveByUserId query);
}
