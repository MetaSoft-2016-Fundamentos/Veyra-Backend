package com.metasoft.veyra.platform.nursing.interfaces.rest;

import com.metasoft.veyra.platform.nursing.domain.model.queries.GetNursingHomeByAdministratorIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetNursingHomeByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.services.NursingHomeCommandServices;
import com.metasoft.veyra.platform.nursing.domain.services.NursingHomeQueryServices;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateNursingHomeResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.NursingHomeResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.CreateNursingHomeCommandFromResourceAssembler;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.NursingHomeFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping(value = "api/v1/administrators/{administratorId}/nursing-homes",produces = APPLICATION_JSON_VALUE)
@Tag(name="Administrators")
public class AdministratorNursingHomesController {
    private final NursingHomeCommandServices nursingHomeCommandServices;
    private final NursingHomeQueryServices nursingHomeQueryServices;
    public AdministratorNursingHomesController(NursingHomeCommandServices nursingHomeCommandServices, NursingHomeQueryServices nursingHomeQueryServices) {
        this.nursingHomeCommandServices = nursingHomeCommandServices;
        this.nursingHomeQueryServices = nursingHomeQueryServices;
    }
    @Operation(
            summary = "Create a new nursing home",
            description = "Creates a new nursing home with the provided business profile information and returns the created nursing home resource"
    )    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Nursing home create"),
            @ApiResponse(responseCode = "400",description = "Bad request")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NursingHomeResource> createNursingHome(@Valid @ModelAttribute CreateNursingHomeResource resource, @PathVariable Long administratorId){
        var createNursingHomeCommand= CreateNursingHomeCommandFromResourceAssembler.toCommandFromResource(resource,administratorId);
        var nursingHomeId=nursingHomeCommandServices.handle(createNursingHomeCommand);
        if (nursingHomeId==null||nursingHomeId==0L){return ResponseEntity.badRequest().build();}
        var getNursingHomeByIdQuery= new GetNursingHomeByIdQuery(nursingHomeId);
        var nursingHome= nursingHomeQueryServices.handle(getNursingHomeByIdQuery);
        if (nursingHome.isEmpty()){return ResponseEntity.notFound().build();}
        var nursingHomeEntity= nursingHome.get();
        var nursingHomeResource= NursingHomeFromEntityAssembler.toResourceFromEntity(nursingHomeEntity);
        return new ResponseEntity<>(nursingHomeResource, HttpStatus.CREATED);
    }
    @GetMapping
    @Operation(
            summary = "Get nursing home by administrator id",
            description = "Returns the nursing home resource associated with the given administrator id. " +
                    "If no nursing home is found, the endpoint responds with 404 Not Found."
    )
    @ApiResponses({
            @ApiResponse( responseCode = "200",  description = "Nursing home found" ),
            @ApiResponse(responseCode = "400", description = "Bad request — invalid administratorId"),
            @ApiResponse(responseCode = "404", description = "Nursing home not found")
    })
    @Parameter(name = "administratorId", description = "Identifier of the administrator who manages the nursing home",required = true)
    public ResponseEntity<NursingHomeResource>getNursingHomeByAdministratorId(@PathVariable  Long administratorId)
    {
        var nursingHome= nursingHomeQueryServices.handle(new GetNursingHomeByAdministratorIdQuery(administratorId));
        if (nursingHome.isEmpty()){return ResponseEntity.notFound().build();}
           var nursingHomeEntity=nursingHome.get();
        var nursingHomeResource=NursingHomeFromEntityAssembler.toResourceFromEntity(nursingHomeEntity);
        return ResponseEntity.ok(nursingHomeResource);
    }
}