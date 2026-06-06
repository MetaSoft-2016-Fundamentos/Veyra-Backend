package com.metasoft.veyra.platform.activities.interfaces.rest;

import com.metasoft.veyra.platform.activities.domain.model.commands.CompleteActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.commands.DeleteActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.queries.GetActivityByIdQuery;
import com.metasoft.veyra.platform.activities.domain.services.ActivityCommandService;
import com.metasoft.veyra.platform.activities.domain.services.ActivityQueryService;
import com.metasoft.veyra.platform.activities.interfaces.rest.resources.ActivityResource;
import com.metasoft.veyra.platform.activities.interfaces.rest.resources.UpdateActivityResource;
import com.metasoft.veyra.platform.activities.interfaces.rest.transform.ActivityResourceFromEntityAssembler;
import com.metasoft.veyra.platform.activities.interfaces.rest.transform.UpdateActivityCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * ActivitiesController
 * <p>Endpoints for updating, deleting and advancing the status of a specific activity.</p>
 */
@RestController
@RequestMapping(value = "/api/v1/activities", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Activities", description = "Activities Management Endpoints")
public class ActivitiesController {

    private final ActivityCommandService activityCommandService;
    private final ActivityQueryService activityQueryService;

    /**
     * Constructor.
     * @param activityCommandService the {@link ActivityCommandService} instance
     * @param activityQueryService the {@link ActivityQueryService} instance
     */
    public ActivitiesController(ActivityCommandService activityCommandService, ActivityQueryService activityQueryService) {
        this.activityCommandService = activityCommandService;
        this.activityQueryService = activityQueryService;
    }

    /**
     * Updates an existing activity.
     * @param activityId the activity ID from the request path
     * @param resource the {@link UpdateActivityResource} with the new values
     * @return the updated {@link ActivityResource}
     */
    @PutMapping("/{activityId}")
    @Operation(summary = "Update activity", description = "Updates type, title, isRecurring and recurringDays of an existing activity.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activity updated"),
            @ApiResponse(responseCode = "400", description = "Activity not found or invalid input")
    })
    public ResponseEntity<ActivityResource> updateActivity(@PathVariable Long activityId, @RequestBody UpdateActivityResource resource) {
        var command = UpdateActivityCommandFromResourceAssembler.toCommandFromResource(activityId, resource);
        var updated = activityCommandService.handle(command);
        if (updated.isEmpty()) return ResponseEntity.badRequest().build();
        var activityResource = ActivityResourceFromEntityAssembler.toResourceFromEntity(updated.get());
        return ResponseEntity.ok(activityResource);
    }

    /**
     * Deletes an activity by its ID.
     * @param activityId the activity ID to delete
     * @return HTTP 204 No Content on success
     */
    @DeleteMapping("/{activityId}")
    @Operation(summary = "Delete activity", description = "Deletes an activity by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Activity deleted"),
            @ApiResponse(responseCode = "400", description = "Activity not found")
    })
    public ResponseEntity<Void> deleteActivity(@PathVariable Long activityId) {
        activityCommandService.handle(new DeleteActivityCommand(activityId));
        return ResponseEntity.noContent().build();
    }

    /**
     * Advances the status of an activity through its lifecycle (PENDING → IN_PROGRESS → COMPLETED).
     * @param activityId the activity ID to advance
     * @return the updated {@link ActivityResource} after the status transition
     */
    @PatchMapping("/{activityId}/complete")
    @Operation(summary = "Advance activity status", description = "PENDING → IN_PROGRESS → COMPLETED.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status advanced"),
            @ApiResponse(responseCode = "400", description = "Activity not found"),
            @ApiResponse(responseCode = "409", description = "Activity already completed")
    })
    public ResponseEntity<ActivityResource> advanceActivityStatus(@PathVariable Long activityId) {
        activityCommandService.handle(new CompleteActivityCommand(activityId));
        var activity = activityQueryService.handle(new GetActivityByIdQuery(activityId));
        if (activity.isEmpty()) return ResponseEntity.badRequest().build();
        var activityResource = ActivityResourceFromEntityAssembler.toResourceFromEntity(activity.get());
        return ResponseEntity.ok(activityResource);
    }
}
