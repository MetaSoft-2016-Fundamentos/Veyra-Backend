package com.metasoft.veyra.platform.payments.interfaces.rest.transform;

import com.metasoft.veyra.platform.payments.domain.model.commands.ProcessPaymentCommand;
import com.metasoft.veyra.platform.payments.interfaces.rest.resources.ProcessPaymentResource;

/**
 * Assembler to transform ProcessPaymentResource into ProcessPaymentCommand.
 * Follows the transformation pattern used across the payments bounded context.
 */
public class ProcessPaymentCommandFromResourceAssembler {

    /**
     * Transforms a ProcessPaymentResource and subscriptionId into a ProcessPaymentCommand.
     *
     * @param subscriptionId The ID of the subscription for which the payment is being processed
     * @param resource The resource containing payment method information
     * @return A ProcessPaymentCommand ready to be handled by the command service
     */
    public static ProcessPaymentCommand toCommandFromResource(
            Long subscriptionId,
            ProcessPaymentResource resource) {

        return new ProcessPaymentCommand(
                subscriptionId,
                resource.paymentMethodId()
        );
    }
}