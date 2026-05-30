package com.metasoft.veyra.platform.nursing.interfaces.rest;

import com.metasoft.veyra.platform.nursing.domain.model.queries.ExistsByNursingHomeIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetAllRelativesByNursingHomeIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetRelativeByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.services.NursingHomeQueryServices;
import com.metasoft.veyra.platform.nursing.domain.services.RelativeCommandService;
import com.metasoft.veyra.platform.nursing.domain.services.RelativeQueryService;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateRelativeResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.RelativeResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.CreateRelativeCommandFromResourceAssembler;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.RelativeResourceFromEntityAssembler;
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

@RestController
@Tag(name = "Nursing Homes")
@RequestMapping(value = "/api/v1/nursingHomes/{nursingHomeId}/relatives", produces = APPLICATION_JSON_VALUE )
public class NursingHomeRelativesController {
private final RelativeCommandService relativeCommandService;
private final RelativeQueryService relativeQueryService;
 private final NursingHomeQueryServices nursingHomeQueryServices;
    public NursingHomeRelativesController(RelativeCommandService relativeCommandService, RelativeQueryService relativeQueryService, NursingHomeQueryServices nursingHomeQueryServices) {
        this.relativeCommandService = relativeCommandService;
        this.relativeQueryService = relativeQueryService;
        this.nursingHomeQueryServices = nursingHomeQueryServices;
    }
    @PostMapping
    @Operation(summary = "Add a relative to nursing home", description = "Create a new relative for the specified nursing home")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "201", description = "Relative created for nursing home"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
     @Parameter(name = "nursingHomeId",description = "The unique identifier of the nursing home", required = true)
    public ResponseEntity<RelativeResource> createRelativeForNursingHome(@PathVariable Long nursingHomeId,@Valid @RequestBody CreateRelativeResource resource){
        var command= CreateRelativeCommandFromResourceAssembler.toCommandFromResource(resource,nursingHomeId);
        var relativeId= relativeCommandService.handle(command);
        if (relativeId==null||relativeId==0L){return ResponseEntity.badRequest().build();}
        var getRelativeByIdQuery=  new GetRelativeByIdQuery(relativeId);
        var relative= relativeQueryService.handle(getRelativeByIdQuery);
        if (relative.isEmpty()){return ResponseEntity.notFound().build();}
        var relativeEntity= relative.get();
        var relativeResource= RelativeResourceFromEntityAssembler.toResourceFromEntity(relativeEntity);
        return new ResponseEntity<>(relativeResource, HttpStatus.CREATED);


    }


    @GetMapping
    @Operation(
            summary = "Get all patient relatives for nursing home",
            description = " Get a list of all patient relatives associated with the specified nursing home"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatives retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Nursing home not found")
    })
    @Parameter(name = "nursingHomeId", description = "The unique identifier of the nursing home", required = true)
    public ResponseEntity<List<RelativeResource>> getAllRelatives(@PathVariable Long nursingHomeId){
        var existsNursingHomeQuery = new ExistsByNursingHomeIdQuery(nursingHomeId);
        if (!nursingHomeQueryServices.handle(existsNursingHomeQuery)) {
            return ResponseEntity.notFound().build();
        }
        var getAllRelativesByNursingHomeIdQuery= new GetAllRelativesByNursingHomeIdQuery(nursingHomeId);
        var relatives= relativeQueryService.handle(getAllRelativesByNursingHomeIdQuery);
        var relativeResources= relatives.stream().map(RelativeResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(relativeResources);
    }

}
