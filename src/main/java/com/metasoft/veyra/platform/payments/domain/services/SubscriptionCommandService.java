package com.metasoft.veyra.platform.payments.domain.services;

import com.metasoft.veyra.platform.payments.domain.model.aggregates.Subscription;
import com.metasoft.veyra.platform.payments.domain.model.commands.CancelSubscriptionCommand;
import com.metasoft.veyra.platform.payments.domain.model.commands.CreateSubscriptionCommand;
import com.metasoft.veyra.platform.payments.domain.model.commands.UpdateSubscriptionCommand;

import java.util.Optional;

public interface SubscriptionCommandService {
    Optional<Subscription> handle(CreateSubscriptionCommand command);
    Optional<Subscription> handle(UpdateSubscriptionCommand command);
    Optional<Subscription> handle(CancelSubscriptionCommand command);
}
