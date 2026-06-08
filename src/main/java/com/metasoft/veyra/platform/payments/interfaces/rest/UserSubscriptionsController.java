package com.metasoft.veyra.platform.payments.interfaces.rest;

import com.metasoft.veyra.platform.payments.domain.model.commands.CancelSubscriptionCommand;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetSubscriptionActiveByUserId;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetSubscriptionByIdQuery;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetSubscriptionByUserId;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.UserId;
import com.metasoft.veyra.platform.payments.domain.services.SubscriptionCommandService;
import com.metasoft.veyra.platform.payments.domain.services.SubscriptionQueryService;
import com.metasoft.veyra.platform.payments.interfaces.rest.resources.CreateSubscriptionResource;
import com.metasoft.veyra.platform.payments.interfaces.rest.resources.SubscriptionResource;
import com.metasoft.veyra.platform.payments.interfaces.rest.resources.UpdateSubscriptionResource;
import com.metasoft.veyra.platform.payments.interfaces.rest.transform.CreateSubscriptionCommandFromResourceAssembler;
import com.metasoft.veyra.platform.payments.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;
import com.metasoft.veyra.platform.payments.interfaces.rest.transform.UpdateSubscriptionCommandFromResourceAssembler;
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
 * REST Controller for managing user subscriptions.
 * Handles subscription creation, retrieval, updates, and cancellation for specific users.
 */
@RestController
@RequestMapping(value = "/api/v1/users/{userId}/subscriptions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Users")
public class UserSubscriptionsController {

    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;

    public UserSubscriptionsController(
            SubscriptionCommandService subscriptionCommandService,
            SubscriptionQueryService subscriptionQueryService) {
        this.subscriptionCommandService = subscriptionCommandService;
        this.subscriptionQueryService = subscriptionQueryService;
    }

    /**
     * POST /api/v1/users/{userId}/subscriptions
     * Creates a new subscription for the specified user.
     */
    @PostMapping
    @Operation(
            summary = "Create a subscription for a user",
            description = "Creates a new subscription with the specified plan type and billing period. " +
                    "Requires a valid Stripe payment method ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data or user already has active subscription"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Parameter(name = "userId", description = "The unique identifier of the user", required = true)
    public ResponseEntity<SubscriptionResource> createSubscription(
            @PathVariable Long userId,
            @Valid @RequestBody CreateSubscriptionResource resource) {

        var command = CreateSubscriptionCommandFromResourceAssembler
                .toCommandFromResource(resource, userId);

        var subscriptionOpt = subscriptionCommandService.handle(command);

        if (subscriptionOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var subscriptionResource = SubscriptionResourceFromEntityAssembler
                .toResourceFromEntity(subscriptionOpt.get());

        return new ResponseEntity<>(subscriptionResource, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/users/{userId}/subscriptions
     * Retrieves all subscriptions for the specified user.
     */
    @GetMapping
    @Operation(
            summary = "Get all subscriptions for a user",
            description = "Returns a list of all subscriptions (active, canceled, past) for the given user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscriptions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No subscriptions found for user")
    })
    @Parameter(name = "userId", description = "The unique identifier of the user", required = true)
    public ResponseEntity<List<SubscriptionResource>> getAllForUser(@PathVariable Long userId) {
        var subscriptions = subscriptionQueryService.handle(new GetSubscriptionByUserId(new UserId(userId)));

        if (subscriptions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var subscriptionResources = subscriptions.stream()
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(subscriptionResources);
    }

    /**
     * GET /api/v1/users/{userId}/subscriptions/active
     * Retrieves the active subscription for the specified user.
     */
    @GetMapping("/active")
    @Operation(
            summary = "Get active subscription for user",
            description = "Returns the currently active subscription for the user, if any exists."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active subscription found"),
            @ApiResponse(responseCode = "404", description = "No active subscription found for user")
    })
    @Parameter(name = "userId", description = "The unique identifier of the user", required = true)
    public ResponseEntity<SubscriptionResource> getActiveSubscription(@PathVariable Long userId) {
        var subscriptionOpt = subscriptionQueryService.handle( new GetSubscriptionActiveByUserId(new UserId(userId)));
        if (subscriptionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var subscriptionResource = SubscriptionResourceFromEntityAssembler
                .toResourceFromEntity(subscriptionOpt.get());

        return ResponseEntity.ok(subscriptionResource);
    }

    /**
     * PUT /api/v1/users/{userId}/subscriptions/{subscriptionId}
     * Updates an existing subscription for the specified user.
     */
    @PutMapping("/{subscriptionId}")
    @Operation(
            summary = "Update a user's subscription",
            description = "Updates the plan type and/or billing period of an existing subscription. " +
                    "Only active subscriptions can be updated. The subscription must belong to the specified user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Cannot update inactive subscription"),
            @ApiResponse(responseCode = "404", description = "Subscription not found or doesn't belong to user")
    })
    @Parameter(name = "userId", description = "The unique identifier of the user", required = true)
    @Parameter(name = "subscriptionId", description = "The unique identifier of the subscription", required = true)
    public ResponseEntity<SubscriptionResource> updateSubscription(
            @PathVariable Long userId,
            @PathVariable Long subscriptionId,
            @Valid @RequestBody UpdateSubscriptionResource resource) {

        var existingSubscriptionOpt = subscriptionQueryService.handle(new GetSubscriptionByIdQuery(subscriptionId));

        if (existingSubscriptionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var existingSubscription = existingSubscriptionOpt.get();
        if (!existingSubscription.getUserId().userId().equals(userId)) {
            return ResponseEntity.notFound().build();
        }

        var command = UpdateSubscriptionCommandFromResourceAssembler
                .toCommandFromResource(subscriptionId, resource);

        var updatedSubscriptionOpt = subscriptionCommandService.handle(command);

        if (updatedSubscriptionOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var subscriptionResource = SubscriptionResourceFromEntityAssembler
                .toResourceFromEntity(updatedSubscriptionOpt.get());

        return ResponseEntity.ok(subscriptionResource);
    }

    /**
     * POST /api/v1/users/{userId}/subscriptions/{subscriptionId}/cancel
     * Cancels an active subscription for the specified user.
     */
    @PostMapping("/{subscriptionId}/cancel")
    @Operation(
            summary = "Cancel a user's subscription",
            description = "Cancels an active subscription. The subscription will remain active until " +
                    "the end of the current billing period. The subscription must belong to the specified user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found or doesn't belong to user")
    })
    @Parameter(name = "userId", description = "The unique identifier of the user", required = true)
    @Parameter(name = "subscriptionId", description = "The unique identifier of the subscription", required = true)
    public ResponseEntity<SubscriptionResource> cancelSubscription(
            @PathVariable Long userId,
            @PathVariable Long subscriptionId) {

        var existingSubscriptionOpt = subscriptionQueryService.handle(new GetSubscriptionByIdQuery(subscriptionId));

        if (existingSubscriptionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var existingSubscription = existingSubscriptionOpt.get();
        if (!existingSubscription.getUserId().userId().equals(userId)) {
            return ResponseEntity.notFound().build();
        }

        var command = new CancelSubscriptionCommand(
                subscriptionId
        );

        var canceledSubscriptionOpt = subscriptionCommandService.handle(command);

        if (canceledSubscriptionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var subscriptionResource = SubscriptionResourceFromEntityAssembler
                .toResourceFromEntity(canceledSubscriptionOpt.get());

        return ResponseEntity.ok(subscriptionResource);
    }
}