package com.metasoft.veyra.platform.tracking.interfaces.rest;

import com.metasoft.veyra.platform.tracking.domain.model.queries.GetAllMeasurementQuery;
import com.metasoft.veyra.platform.tracking.domain.services.MeasurementQueryService;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.MeasurementResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.transform.MeasurementResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/measurements",produces = APPLICATION_JSON_VALUE)
@Tag(name = "Measurements", description = "Endpoints for managing measurements")
public class MeasurementsController {
    private final MeasurementQueryService measurementQueryService;

    public MeasurementsController(MeasurementQueryService measurementQueryService) {
        this.measurementQueryService = measurementQueryService;
    }

    @GetMapping
    @Operation(summary = "Get all measurements", description = "Retrieve a list of all measurements")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "successful operation"),
        @ApiResponse(responseCode = "400",description = "not found ")
})
    public ResponseEntity<List<MeasurementResource>>getAllMeasurements()
{
    var query=measurementQueryService.handle(new GetAllMeasurementQuery());
    if (query.isEmpty()){return ResponseEntity.badRequest().build();}
    var resource= query.stream().map(MeasurementResourceFromEntityAssembler::toResourceFromEntity).toList();
    return ResponseEntity.ok(resource);
}
}
