package com.metasoft.veyra.platform.activities.interfaces.rest;

import com.metasoft.veyra.platform.activities.domain.model.queries.GetActivitiesByDateAndNursingHomeQuery;
import com.metasoft.veyra.platform.activities.domain.services.ActivityCommandService;
import com.metasoft.veyra.platform.activities.domain.services.ActivityQueryService;
import com.metasoft.veyra.platform.activities.interfaces.rest.resources.ActivityResource;
import com.metasoft.veyra.platform.activities.interfaces.rest.resources.CreateActivityResource;
import com.metasoft.veyra.platform.activities.interfaces.rest.transform.ActivityResourceFromViewAssembler;
import com.metasoft.veyra.platform.activities.interfaces.rest.transform.CreateActivityCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/nursing-homes/{nursingHomeId}/activities", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Activities", description = "Activities Management Endpoints")
public class ActivitiesController {

    private final ActivityCommandService activityCommandService;
    private final ActivityQueryService activityQueryService;

    public ActivitiesController(ActivityCommandService activityCommandService, ActivityQueryService activityQueryService) {
        this.activityCommandService = activityCommandService;
        this.activityQueryService = activityQueryService;
    }


    @GetMapping
    public ResponseEntity<List<ActivityResource>> getActivitiesForDay(
            @PathVariable Long nursingHomeId,
            @RequestParam("date") LocalDate date) {

        var query = new GetActivitiesByDateAndNursingHomeQuery(date, nursingHomeId);
        var activityViews = activityQueryService.handle(query);

        if (activityViews.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        var activityResources = activityViews.stream()
                .map(ActivityResourceFromViewAssembler::toResourceFromView)
                .toList();
        return ResponseEntity.ok(activityResources);
    }

    @PostMapping
    public ResponseEntity<Long> createActivity(
            @PathVariable Long nursingHomeId,
            @RequestBody CreateActivityResource resource) {

        var command = CreateActivityCommandFromResourceAssembler.toCommandFromResource(resource, nursingHomeId);
        var activityId = activityCommandService.handle(command);

        if (activityId == 0L) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(activityId);
    }
}