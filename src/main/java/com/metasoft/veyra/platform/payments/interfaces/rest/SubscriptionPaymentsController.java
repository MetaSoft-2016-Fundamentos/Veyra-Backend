package com.metasoft.veyra.platform.payments.interfaces.rest;

import com.metasoft.veyra.platform.payments.domain.model.queries.GetPaymentBySubscriptionIdQuery;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetSubscriptionByIdQuery;
import com.metasoft.veyra.platform.payments.domain.services.PaymentCommandService;
import com.metasoft.veyra.platform.payments.domain.services.PaymentQueryService;
import com.metasoft.veyra.platform.payments.domain.services.SubscriptionQueryService;
import com.metasoft.veyra.platform.payments.interfaces.rest.resources.PaymentResource;
import com.metasoft.veyra.platform.payments.interfaces.rest.resources.ProcessPaymentResource;
import com.metasoft.veyra.platform.payments.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
import com.metasoft.veyra.platform.payments.interfaces.rest.transform.ProcessPaymentCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST Controller for managing payments associated with subscriptions.
 * Handles payment processing and retrieval for specific subscriptions.
 */
@RestController
@RequestMapping(value = "/api/v1/subscriptions/{subscriptionId}/payments", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Subscriptions", description = "Endpoints for managing payments of a subscription")
public class SubscriptionPaymentsController {

    private final PaymentCommandService paymentCommandService;
    private final PaymentQueryService paymentQueryService;
    private final SubscriptionQueryService subscriptionQueryService;

    public SubscriptionPaymentsController(
            PaymentCommandService paymentCommandService,
            PaymentQueryService paymentQueryService,
            SubscriptionQueryService subscriptionQueryService) {
        this.paymentCommandService = paymentCommandService;
        this.paymentQueryService = paymentQueryService;
        this.subscriptionQueryService = subscriptionQueryService;
    }

    /**
     * POST /api/v1/subscriptions/{subscriptionId}/payments
     * Processes a new payment for the specified subscription.
     */
    @PostMapping
    @Operation(
            summary = "Process a payment for a subscription",
            description = "Processes a payment for the given subscription using the provided payment method. " +
                    "Creates a PaymentIntent in Stripe and records the payment in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid payment data"),
            @ApiResponse(responseCode = "404", description = "Subscription not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Parameter(name = "subscriptionId", description = "The unique identifier of the subscription", required = true)
    public ResponseEntity<PaymentResource> processPayment(
            @PathVariable Long subscriptionId,
            @Valid @RequestBody ProcessPaymentResource resource) {

        var subscriptionOpt = subscriptionQueryService.handle(
                new GetSubscriptionByIdQuery(subscriptionId)
        );

        if (subscriptionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var command = ProcessPaymentCommandFromResourceAssembler
                .toCommandFromResource(subscriptionId, resource);

        var paymentOpt = paymentCommandService.handle(command);

        if (paymentOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var paymentResource = PaymentResourceFromEntityAssembler
                .toResourceFromEntity(paymentOpt.get());

        return new ResponseEntity<>(paymentResource, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/subscriptions/{subscriptionId}/payments
     * Retrieves all payments for the specified subscription.
     */
    @GetMapping
    @Operation(
            summary = "List all payments for a subscription",
            description = "Returns a list of all payments (successful, failed, pending) for the given subscription."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found or no payments exist")
    })
    @Parameter(name = "subscriptionId", description = "The unique identifier of the subscription", required = true)
    public ResponseEntity<List<PaymentResource>> getAllPaymentsBySubscription(
            @PathVariable Long subscriptionId) {
        var subscriptionOpt = subscriptionQueryService.handle(new GetSubscriptionByIdQuery(subscriptionId));

        if (subscriptionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var payments = paymentQueryService.handle(new GetPaymentBySubscriptionIdQuery(subscriptionId));
        if (payments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var paymentResources = payments.stream()
                .map(PaymentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(paymentResources);
    }
}