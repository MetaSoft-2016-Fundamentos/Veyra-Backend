package com.metasoft.veyra.platform.nursing.interfaces.rest;

import com.metasoft.veyra.platform.nursing.domain.model.queries.GetAllRelativesQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetRelativeByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.services.RelativeCommandService;
import com.metasoft.veyra.platform.nursing.domain.services.RelativeQueryService;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateRelativeResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.RelativeResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.CreateRelativeCommandFromResourceAssembler;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.RelativeResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
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
 * REST controller that exposes endpoints to manage patient relatives.
 * This controller provides functionality to create a new relative
 * associated with a patient in the nursing platform.
 */
@RestController
@RequestMapping(value = "/api/v1/relatives",produces = APPLICATION_JSON_VALUE)
 @Tag(name ="Relatives",description = "Available endpoints for relatives")

public class RelativesController {
private final RelativeCommandService relativeCommandService;
private final RelativeQueryService relativeQueryService;
    /**
     * Creates a new {@link RelativesController} instance.
     *
     * @param relativeCommandService Service to handle relative creation commands
     * @param relativeQueryService   Service to query relative information
     */
    public RelativesController(RelativeCommandService relativeCommandService, RelativeQueryService relativeQueryService) {
        this.relativeCommandService = relativeCommandService;
        this.relativeQueryService = relativeQueryService;
    }
    /**
     * Endpoint to create a new relative for a patient.
     *
     * @param resource DTO containing the necessary information to create a relative
     * @return {@link ResponseEntity} containing the created {@link RelativeResource}, or an error status
     */
    @PostMapping
    @Operation(
            summary = "Create a new patient relative",
            description = "Creates a relative entity and returns its stored details if the operation is successful"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Relative created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or error during creation"),
            @ApiResponse(responseCode = "404", description = "Relative not found after creation")
    })

public ResponseEntity<RelativeResource>createRelative(@Valid @RequestBody CreateRelativeResource resource){
        var createRelativeCommand= CreateRelativeCommandFromResourceAssembler.toCommandFromResource(resource);
        var relativeId= relativeCommandService.handle(createRelativeCommand);
        if (relativeId==null|| relativeId==0L){return ResponseEntity.badRequest().build();}
         var getRelativeByIdQuery=new GetRelativeByIdQuery(relativeId);
        var relative=relativeQueryService.handle(getRelativeByIdQuery);
        if (relative.isEmpty()){return ResponseEntity.notFound().build();}
        var relativeEntity= relative.get();
        var relativeResource= RelativeResourceFromEntityAssembler.toResourceFromEntity(relativeEntity);
        return new ResponseEntity<>(relativeResource, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "Get all patient relatives",
            description = "Retrieves a list of all relatives associated with patients in the nursing platform"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of relatives retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No relatives found")
    })
    public ResponseEntity<List<RelativeResource>> getAllRelatives(){
    var relatives= relativeQueryService.handle(new GetAllRelativesQuery());
    if (relatives.isEmpty()){return ResponseEntity.notFound().build();}
    var RelativeResource= relatives.stream().map(RelativeResourceFromEntityAssembler::toResourceFromEntity).toList();
    return ResponseEntity.ok(RelativeResource);
    }
}
