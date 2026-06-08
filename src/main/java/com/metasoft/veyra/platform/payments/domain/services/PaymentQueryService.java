package com.metasoft.veyra.platform.payments.domain.services;

import com.metasoft.veyra.platform.payments.domain.model.aggregates.Payment;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetPaymentByIdQuery;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetPaymentByStripePaymentIntentIdQuery;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetPaymentBySubscriptionIdQuery;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetPaymentsByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface PaymentQueryService {
    Optional<Payment> findById(GetPaymentByIdQuery query);
    List<Payment> handle(GetPaymentsByUserIdQuery query);
    List<Payment> handle(GetPaymentBySubscriptionIdQuery query);
    Optional<Payment> handle(GetPaymentByStripePaymentIntentIdQuery query);
}
