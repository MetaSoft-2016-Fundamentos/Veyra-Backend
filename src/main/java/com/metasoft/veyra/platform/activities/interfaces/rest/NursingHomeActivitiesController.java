package com.metasoft.veyra.platform.activities.interfaces.rest;

import com.metasoft.veyra.platform.activities.domain.model.queries.GetActivitiesByNursingHomeIdQuery;
import com.metasoft.veyra.platform.activities.domain.model.queries.GetActivityByIdQuery;
import com.metasoft.veyra.platform.activities.domain.services.ActivityCommandService;
import com.metasoft.veyra.platform.activities.domain.services.ActivityQueryService;
import com.metasoft.veyra.platform.activities.interfaces.rest.resources.ActivityResource;
import com.metasoft.veyra.platform.activities.interfaces.rest.resources.CreateActivityResource;
import com.metasoft.veyra.platform.activities.interfaces.rest.transform.ActivityResourceFromEntityAssembler;
import com.metasoft.veyra.platform.activities.interfaces.rest.transform.CreateActivityCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * NursingHomeActivitiesController
 * <p>Endpoints for listing and creating activities scoped to a nursing home.</p>
 */
@RestController
@RequestMapping(value = "/api/v1/nursing-homes/{nursingHomeId}/activities", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Nursing Homes", description = "Nursing Home Activities Endpoints")
public class NursingHomeActivitiesController {

    private final ActivityCommandService activityCommandService;
    private final ActivityQueryService activityQueryService;

    /**
     * Constructor.
     * @param activityCommandService the {@link ActivityCommandService} instance
     * @param activityQueryService the {@link ActivityQueryService} instance
     */
    public NursingHomeActivitiesController(ActivityCommandService activityCommandService, ActivityQueryService activityQueryService) {
        this.activityCommandService = activityCommandService;
        this.activityQueryService = activityQueryService;
    }

    /**
     * Returns all activities for a given nursing home.
     * @param nursingHomeId the nursing home ID from the request path
     * @return a list of {@link ActivityResource} for the nursing home
     */
    @GetMapping
    @Operation(summary = "Get activities by nursing home", description = "Returns all activities for a given nursing home.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Activities retrieved successfully"))
    public ResponseEntity<List<ActivityResource>> getActivitiesByNursingHome(@PathVariable Long nursingHomeId) {
        var activities = activityQueryService.handle(new GetActivitiesByNursingHomeIdQuery(nursingHomeId));
        var resources = activities.stream()
                .map(ActivityResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Creates a new activity within the given nursing home.
     * @param nursingHomeId the nursing home ID from the request path
     * @param resource the {@link CreateActivityResource} with the activity data
     * @return the created {@link ActivityResource} with HTTP 201
     */
    @PostMapping
    @Operation(summary = "Create activity", description = "Registers a new activity with status PENDING.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Activity created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<ActivityResource> createActivity(@PathVariable Long nursingHomeId, @RequestBody CreateActivityResource resource) {
        var command = CreateActivityCommandFromResourceAssembler.toCommandFromResource(nursingHomeId, resource);
        var activityId = activityCommandService.handle(command);
        if (activityId == null || activityId == 0L) return ResponseEntity.badRequest().build();
        var activity = activityQueryService.handle(new GetActivityByIdQuery(activityId));
        if (activity.isEmpty()) return ResponseEntity.badRequest().build();
        var activityResource = ActivityResourceFromEntityAssembler.toResourceFromEntity(activity.get());
        return new ResponseEntity<>(activityResource, HttpStatus.CREATED);
    }
}
