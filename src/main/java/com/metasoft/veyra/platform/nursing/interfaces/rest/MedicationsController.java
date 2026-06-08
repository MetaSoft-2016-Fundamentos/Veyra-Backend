package com.metasoft.veyra.platform.nursing.interfaces.rest;

import com.metasoft.veyra.platform.nursing.domain.model.queries.GetMedicationByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.services.MedicationQueryServices;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.MedicationResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.MedicationResourceFromEntityAssembler;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/medications",produces = APPLICATION_JSON_VALUE)
@Tag(name = "Medications", description = "Available medication endpoints")
public class MedicationsController {
    private final MedicationQueryServices medicationQueryServices;

    public MedicationsController( MedicationQueryServices medicationQueryServices) {
        this.medicationQueryServices = medicationQueryServices;
    }
    @GetMapping("/{medicationId}")
    @Operation(summary = "Get medication by ID",description = "Get medication by ID")
    @ApiResponses(value={
       @ApiResponse(responseCode = "200",description = "Medication found"),
       @ApiResponse(responseCode = "404",description = "Medication not found")
    })
    @Parameter(name = "id", description = " The unique identifier of the medication",required = true)
    public ResponseEntity<MedicationResource>getMedicationById(@PathVariable Long medicationId)
    {
        var medicationQuery=medicationQueryServices.handle(new GetMedicationByIdQuery(medicationId));
        if (medicationQuery.isEmpty()){return ResponseEntity.notFound().build();}
        var medicationEntity= medicationQuery.get();
        var medicationResource=MedicationResourceFromEntityAssembler.toResourceFromEntity(medicationEntity);
        return ResponseEntity.ok(medicationResource);
    }
}
