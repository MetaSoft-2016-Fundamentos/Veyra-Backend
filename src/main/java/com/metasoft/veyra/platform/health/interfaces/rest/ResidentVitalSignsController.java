package com.metasoft.veyra.platform.health.interfaces.rest;

import com.metasoft.veyra.platform.health.domain.model.queries.GetVitalSignsByResidentIdQuery;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.health.domain.services.VitalSignQueryService;
import com.metasoft.veyra.platform.health.interfaces.rest.resources.VitalSignResource;
import com.metasoft.veyra.platform.health.interfaces.rest.transform.VitalSignResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/resident/{residentId}/vital-signs",produces = APPLICATION_JSON_VALUE)
@RestController
@Tag(name = "Residents")
public class ResidentVitalSignsController {
private final VitalSignQueryService vitalSignQueryService;

    public ResidentVitalSignsController( VitalSignQueryService vitalSignQueryService) {
        this.vitalSignQueryService = vitalSignQueryService;
    }

    @GetMapping
    @Operation(
            summary = "Get vital signs for a resident",
            description = "Returns the list of vital signs associated with a resident identified by the given resident ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of vital signs found (may be an empty list)."),
            @ApiResponse(responseCode = "404", description = "Resident not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @Parameter(name = "residentId", description = "The unique identifier of the resident ", required = true)
    public ResponseEntity<List<VitalSignResource>> getVitalSignsByResidentId(
            @PathVariable Long residentId) {

        var vitalSigns = vitalSignQueryService.handle(
                new GetVitalSignsByResidentIdQuery(new ResidentId(residentId))
        );

        if (vitalSigns == null || vitalSigns.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        var resources = vitalSigns.stream()
                .map(VitalSignResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}
