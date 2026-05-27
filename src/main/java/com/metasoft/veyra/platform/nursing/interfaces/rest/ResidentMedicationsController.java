package com.metasoft.veyra.platform.nursing.interfaces.rest;

import com.metasoft.veyra.platform.nursing.domain.model.queries.ExistsByResidentIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetAllMedicationsByResidentIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetMedicationByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.services.MedicationCommandServices;
import com.metasoft.veyra.platform.nursing.domain.services.MedicationQueryServices;
import com.metasoft.veyra.platform.nursing.domain.services.ResidentQueryServices;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateMedicationResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.MedicationResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.CreateMedicationCommandFromResourceAssembler;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.MedicationResourceFromEntityAssembler;
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
@RequestMapping(value = "/api/v1/residents/{residentId}/medications",produces = APPLICATION_JSON_VALUE)
@Tag(name = "Residents")
public class ResidentMedicationsController {
    private final MedicationQueryServices medicationQueryServices;
    private final MedicationCommandServices medicationCommandServices;
    private final ResidentQueryServices residentQueryServices;

    public ResidentMedicationsController(MedicationQueryServices medicationQueryServices, MedicationCommandServices medicationCommandServices, ResidentQueryServices residentQueryServices) {
        this.medicationQueryServices = medicationQueryServices;
        this.medicationCommandServices = medicationCommandServices;
        this.residentQueryServices = residentQueryServices;
    }

    @GetMapping
    @Operation(summary = "Get medications for resident",description = "Get the medications for a resident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Medications  retrieved successfully"),
            @ApiResponse(responseCode = "404",description = "Resident not found")
    })
    @Parameter(name = "residentId", description = " The unique identifier of the resident",required = true)
    public ResponseEntity<List<MedicationResource>> getAllMedicationsByResidentId(@PathVariable Long residentId) {
        var existsByResidentIdQuery = new ExistsByResidentIdQuery(residentId);
        if (!residentQueryServices.handle(existsByResidentIdQuery)) {
            return ResponseEntity.notFound().build();
        }
        var getAllMedicationsByResidentIdQuery = new GetAllMedicationsByResidentIdQuery(existsByResidentIdQuery.residentId());
       var medications= medicationQueryServices.handle(getAllMedicationsByResidentIdQuery);
       var medicationResource=medications.stream().map(MedicationResourceFromEntityAssembler::toResourceFromEntity).toList();
       return ResponseEntity.ok(medicationResource);
    }
    @PostMapping
    @Operation(summary = "Create a new medication for resident",description = "Create a new medication for resident")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "201",description = " Medication created"),
                    @ApiResponse(responseCode = "400",description = "Bad request")
            })
    @Parameter(name = "residentId",description = "The unique identifier of the resident", required = true)
    public ResponseEntity<MedicationResource>createMedicationForResident(@PathVariable Long residentId, @Valid @RequestBody CreateMedicationResource resource)
    {
        var medicationCommandAssembler= CreateMedicationCommandFromResourceAssembler.toCommandFromResource(resource,residentId);
        var medicationCommand=medicationCommandServices.handle(medicationCommandAssembler);
        if (medicationCommand==null||medicationCommand==0L){return ResponseEntity.badRequest().build();}
        var medicationId= new GetMedicationByIdQuery(medicationCommand);
        var medicationFindByIdQuery= medicationQueryServices.handle(medicationId);
        if (medicationFindByIdQuery.isEmpty()){return ResponseEntity.notFound().build();}
        var medicationResource= MedicationResourceFromEntityAssembler.toResourceFromEntity(medicationFindByIdQuery.get());
        return new ResponseEntity<>(medicationResource, HttpStatus.CREATED);
    }
}
