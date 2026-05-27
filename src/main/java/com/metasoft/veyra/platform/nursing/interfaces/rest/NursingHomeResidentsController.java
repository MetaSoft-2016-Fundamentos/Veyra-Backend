package com.metasoft.veyra.platform.nursing.interfaces.rest;

import com.metasoft.veyra.platform.nursing.domain.model.queries.ExistsByNursingHomeIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetAllResidentsByNursingHomeIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetResidentByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.services.NursingHomeQueryServices;
import com.metasoft.veyra.platform.nursing.domain.services.ResidentCommandServices;
import com.metasoft.veyra.platform.nursing.domain.services.ResidentQueryServices;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateResidentResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.ResidentResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.CreateResidentCommandFromResourceAssembler;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.ResidentResourceFromEntityAssembler;
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
@RequestMapping(value = "/api/v1/nursing-homes/{nursingHomeId}/residents",produces = APPLICATION_JSON_VALUE)
@Tag(name = "Nursing Homes")
public class NursingHomeResidentsController {
    private final ResidentQueryServices residentQueryServices;
    private final ResidentCommandServices residentCommandServices;
    private final NursingHomeQueryServices nursingHomeQueryServices;

    public NursingHomeResidentsController(ResidentQueryServices residentQueryServices, ResidentCommandServices residentCommandServices, NursingHomeQueryServices nursingHomeQueryServices) {
        this.residentQueryServices = residentQueryServices;
        this.residentCommandServices = residentCommandServices;
        this.nursingHomeQueryServices = nursingHomeQueryServices;
    }

    @PostMapping
    @Operation(summary = "Create a new resident in a nursing home")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Resident created"),
            @ApiResponse(responseCode = "400",description = "Bad request")
    })
    @Parameter(name = "nursingHomeId", description = "The unique identifier of the nursing home", required = true)
    public ResponseEntity<ResidentResource> createResident(@Valid @RequestBody CreateResidentResource resource, @PathVariable Long nursingHomeId)
    {
        var residentCommand= CreateResidentCommandFromResourceAssembler.toCommandFromResource(resource,nursingHomeId);
        var residentId= residentCommandServices.handle(residentCommand);
        if (residentId==null||residentId==0L){return ResponseEntity.badRequest().build();}
        var getResidentByIdQuery=new GetResidentByIdQuery(residentId);
        var resident= residentQueryServices.handle(getResidentByIdQuery);
        if (resident.isEmpty()){return ResponseEntity.notFound().build();}
        var residentEntity=resident.get();
        var residentResource= ResidentResourceFromEntityAssembler.toResourceFromEntity(residentEntity);
        return new ResponseEntity<>(residentResource, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get residents for nursing home", description = "Get the residents for a nursing home")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Residents retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Nursing home not found")
    })
    @Parameter(name = "nursingHomeId", description = "The unique identifier of the nursing home", required = true)
    public ResponseEntity<List<ResidentResource>> getAllResidents(@PathVariable Long nursingHomeId) {
        var existsNursingHomeQuery = new ExistsByNursingHomeIdQuery(nursingHomeId);
        if (!nursingHomeQueryServices.handle(existsNursingHomeQuery)) {
            return ResponseEntity.notFound().build();
        }

        var getAllResidentsByNursingHomeIdQuery = new GetAllResidentsByNursingHomeIdQuery(nursingHomeId);
        var residents = residentQueryServices.handle(getAllResidentsByNursingHomeIdQuery);
        var residentResources = residents.stream()
                .map(ResidentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(residentResources);
    }
}

