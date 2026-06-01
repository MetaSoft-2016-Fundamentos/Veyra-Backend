package com.metasoft.veyra.platform.payments.interfaces.rest.transform;

import com.metasoft.veyra.platform.payments.domain.model.commands.UpdateSubscriptionCommand;
import com.metasoft.veyra.platform.payments.interfaces.rest.resources.UpdateSubscriptionResource;

public class UpdateSubscriptionCommandFromResourceAssembler {
    public static UpdateSubscriptionCommand toCommandFromResource(Long subscriptionId, UpdateSubscriptionResource resource) {
        return new UpdateSubscriptionCommand(subscriptionId, resource.planType(), resource.period());
    }
}
