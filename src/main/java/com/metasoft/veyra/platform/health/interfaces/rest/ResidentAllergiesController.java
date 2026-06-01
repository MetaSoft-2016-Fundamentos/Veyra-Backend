package com.metasoft.veyra.platform.health.interfaces.rest;
import com.metasoft.veyra.platform.health.domain.model.queries.GetAllergiesByResidentIdQuery;
import com.metasoft.veyra.platform.health.domain.model.queries.GetAllergyByIdQuery;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.health.domain.services.AllergyCommandService;
import com.metasoft.veyra.platform.health.domain.services.AllergyQueryService;
import com.metasoft.veyra.platform.health.interfaces.rest.resources.AllergyResource;
import com.metasoft.veyra.platform.health.interfaces.rest.resources.RegisterAllergyResource;
import com.metasoft.veyra.platform.health.interfaces.rest.transform.AllergyResourceFromEntityAssembler;
import com.metasoft.veyra.platform.health.interfaces.rest.transform.RegisterAllergyCommandFromResourceAssembler;
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
@RequestMapping(value = "/api/v1/residents/{residentId}/allergies",produces = APPLICATION_JSON_VALUE)
@Tag(name="Residents")
public class ResidentAllergiesController {
private final AllergyCommandService allergyCommandService;
private final AllergyQueryService allergyQueryService;
    public ResidentAllergiesController(AllergyCommandService allergyCommandService, AllergyQueryService allergyQueryService) {
        this.allergyCommandService = allergyCommandService;
        this.allergyQueryService = allergyQueryService;
    }
    @PostMapping
    @Operation(
            summary = "Register a new allergy for a resident",
            description = "Creates a new allergy record associated with the specified resident and returns the created resource."
    )
    @Parameter(
            name = "residentId",
            description = "Identifier of the resident",
            required = true
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = " register new allergy"),@ApiResponse(responseCode = "400",description = "Bad Request")
    })
    public ResponseEntity<AllergyResource>registerAllergy(@Valid @RequestBody RegisterAllergyResource resource, @PathVariable Long residentId){
    var createCommand= RegisterAllergyCommandFromResourceAssembler.toCommandFromResource(resource,residentId);
     var allergyId= allergyCommandService.handle(createCommand);
     if (allergyId==null||allergyId==0L){return ResponseEntity.badRequest().build();}
     var getAllergyByIdQuery= new GetAllergyByIdQuery(allergyId);
     var query=allergyQueryService.handle(getAllergyByIdQuery);
     if (query.isEmpty()){return ResponseEntity.notFound().build();}
     var allergyEntity= query.get();
     var allergyResource= AllergyResourceFromEntityAssembler.toResourceFromEntity(allergyEntity);
     return new ResponseEntity<>(allergyResource, HttpStatus.CREATED);
}
@GetMapping
@Operation(summary = "Get all allergies for a resident", description = "Retrieves a list of all allergies associated with the specified resident.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "Allergies successfully retrieved"),
        @ApiResponse(responseCode = "404",description = "Resident not found or no allergies available")
})
    public ResponseEntity<List<AllergyResource>>getAllAllergies(@PathVariable Long residentId){
        var resident= new ResidentId(residentId);
        var query= allergyQueryService.handle( new GetAllergiesByResidentIdQuery(resident));
        if (query.isEmpty()){return ResponseEntity.notFound().build();}
        var resource= query.stream().map(AllergyResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resource);
}

}
