package com.metasoft.veyra.platform.payments.interfaces.rest.transform;

import com.metasoft.veyra.platform.payments.domain.model.commands.CreateSubscriptionCommand;
import com.metasoft.veyra.platform.payments.interfaces.rest.resources.CreateSubscriptionResource;

public class CreateSubscriptionCommandFromResourceAssembler {

    public static CreateSubscriptionCommand toCommandFromResource(
            CreateSubscriptionResource resource, Long userId) {

        return new CreateSubscriptionCommand(
                userId, resource.planType(), resource.period(),
                resource.paymentMethodId()
        );
    }
}
