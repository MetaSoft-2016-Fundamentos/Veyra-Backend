package com.metasoft.veyra.platform.nursing.interfaces.rest;

import com.metasoft.veyra.platform.nursing.domain.model.commands.DeleteResidentCommand;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetResidentByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.services.ResidentCommandServices;
import com.metasoft.veyra.platform.nursing.domain.services.ResidentQueryServices;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.ResidentResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.UpdateResidentResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.ResidentResourceFromEntityAssembler;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.UpdateResidentCommandFromResourceAssembler;
import com.metasoft.veyra.platform.shared.interfaces.rest.resources.MessageResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/residents", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Residents",description = "Available resident endpoints")
public class ResidentsController {
private final ResidentCommandServices residentCommandServices;
private final ResidentQueryServices residentQueryServices;

    public ResidentsController(ResidentCommandServices residentCommandServices, ResidentQueryServices residentQueryServices) {
        this.residentCommandServices = residentCommandServices;
        this.residentQueryServices = residentQueryServices;
    }

    @PutMapping(value = "/{residentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update resident by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resident updated"),
            @ApiResponse(responseCode = "404", description = "Resident not found")
    })
    @Parameter(name = "residentId", description = "The unique identifier of the resident", required = true)
    public ResponseEntity<ResidentResource> updateResident(@PathVariable Long residentId, @Valid @ModelAttribute UpdateResidentResource resource)
    {
      var updateResidentCommand= UpdateResidentCommandFromResourceAssembler.toCommandFromResource(residentId,resource);
      var updateResident= residentCommandServices.handle(updateResidentCommand);
      if (updateResident.isEmpty()){return ResponseEntity.notFound().build();}
      var residentEntity=updateResident.get();
      var residentResource= ResidentResourceFromEntityAssembler.toResourceFromEntity(residentEntity);
      return ResponseEntity.ok(residentResource);
    }
    @GetMapping("/{residentId}")
    @Operation(summary = "Get resident by ID")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "resident found "),
            @ApiResponse(responseCode = "404",description = "resident not found")
    })
    @Parameter(name = "residentId", description = " The unique identifier of the resident",required = true)
    public ResponseEntity<ResidentResource>getResidentById(@PathVariable Long residentId){
        var resident= residentQueryServices.handle(new GetResidentByIdQuery(residentId));
        if (resident.isEmpty()){return ResponseEntity.notFound().build();}
        var residentEntity= resident.get();
        var residentResource= ResidentResourceFromEntityAssembler.toResourceFromEntity(residentEntity);
        return ResponseEntity.ok(residentResource);
    }

    @DeleteMapping("/{residentId}")
    @Operation(summary = "Delete resident by ID",description = "Delete resident")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Resident deleted"),
            @ApiResponse(responseCode = "404",description = "Resident not found")
    })
    @Parameter(name = "residentId", description = "The unique identifier of the resident", required = true)
    public ResponseEntity<?>deleteResident( @PathVariable Long residentId){
     var deleteResidentCommand= new DeleteResidentCommand(residentId);
      residentCommandServices.handle(deleteResidentCommand);
      return ResponseEntity.ok(new MessageResource("Resident with given id successfully deleted"));
    }



}
