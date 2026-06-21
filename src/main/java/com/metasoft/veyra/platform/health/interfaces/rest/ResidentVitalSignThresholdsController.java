package com.metasoft.veyra.platform.health.interfaces.rest;

import com.metasoft.veyra.platform.health.domain.model.commands.RegisterVitalSignThresholdCommand;
import com.metasoft.veyra.platform.health.domain.model.queries.GetVitalSignThresholdByResidentIdQuery;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.health.domain.services.VitalSignThresholdCommandService;
import com.metasoft.veyra.platform.health.domain.services.VitalSignThresholdQueryService;
import com.metasoft.veyra.platform.health.interfaces.rest.resources.RegisterVitalSignThresholdResource;
import com.metasoft.veyra.platform.health.interfaces.rest.resources.VitalSignThresholdResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/residents/{residentId}/vital-sign-thresholds", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Residents")
public class ResidentVitalSignThresholdsController {

    private final VitalSignThresholdCommandService commandService;
    private final VitalSignThresholdQueryService queryService;

    public ResidentVitalSignThresholdsController(
            VitalSignThresholdCommandService commandService,
            VitalSignThresholdQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PutMapping
    @Operation(
        summary = "Set vital sign thresholds for a resident",
        description = "Creates or updates the doctor-defined thresholds used to evaluate this resident's vital signs."
    )
    @ApiResponse(responseCode = "200", description = "Thresholds saved successfully")
    public ResponseEntity<VitalSignThresholdResource> setThresholds(
            @PathVariable Long residentId,
            @RequestBody RegisterVitalSignThresholdResource resource) {

        var command = new RegisterVitalSignThresholdCommand(
                residentId,
                resource.heartRateMin(), resource.heartRateMax(),
                resource.systolicMax(), resource.diastolicMax(),
                resource.temperatureMin(), resource.temperatureMax(),
                resource.oxygenSaturationMin(),
                resource.respiratoryRateMin(), resource.respiratoryRateMax()
        );
        var threshold = commandService.handle(command);
        return ResponseEntity.ok(toResource(threshold));
    }

    @GetMapping
    @Operation(
        summary = "Get vital sign thresholds for a resident",
        description = "Returns the current thresholds configured by the doctor for this resident."
    )
    @ApiResponse(responseCode = "200", description = "Thresholds found")
    @ApiResponse(responseCode = "404", description = "No thresholds configured for this resident")
    public ResponseEntity<VitalSignThresholdResource> getThresholds(@PathVariable Long residentId) {
        return queryService.handle(new GetVitalSignThresholdByResidentIdQuery(new ResidentId(residentId)))
                .map(t -> ResponseEntity.ok(toResource(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    private VitalSignThresholdResource toResource(
            com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSignThreshold t) {
        return new VitalSignThresholdResource(
                t.getId(),
                t.getResidentId().residentId(),
                t.getHeartRateMin(), t.getHeartRateMax(),
                t.getSystolicMax(), t.getDiastolicMax(),
                t.getTemperatureMin(), t.getTemperatureMax(),
                t.getOxygenSaturationMin(),
                t.getRespiratoryRateMin(), t.getRespiratoryRateMax()
        );
    }
}
