package com.metasoft.veyra.platform.nursing.interfaces.rest;

import com.metasoft.veyra.platform.nursing.domain.model.queries.GetAdministratorByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetAdministratorByUserIdQuery;
import com.metasoft.veyra.platform.nursing.domain.services.AdministratorCommandService;
import com.metasoft.veyra.platform.nursing.domain.services.AdministratorQueryService;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.AdministratorResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateAdministratorResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.AdministratorResourceFromEntityAssembler;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.CreateAdministratorCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping(value = "/api/v1/administrators",produces = APPLICATION_JSON_VALUE)
@Tag(name = "Administrators",description = " Available endpoints for administrators")
public class AdministratorsController {
  private final AdministratorCommandService administratorCommandService;
  private final AdministratorQueryService administratorQueryService;

  public AdministratorsController(AdministratorCommandService administratorCommandService, AdministratorQueryService administratorQueryService) {
    this.administratorCommandService = administratorCommandService;
    this.administratorQueryService = administratorQueryService;
  }

  @PostMapping
  @Operation(summary = "Create administrator", description = "Creates a new Administrator and returns its resource representation.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Administrator created successfully"),
    @ApiResponse(responseCode = "400", description = "Bad request")
  })

  public ResponseEntity<AdministratorResource>createAdministrator(@Valid @RequestBody CreateAdministratorResource resource){
    var createAdministratorCommand= CreateAdministratorCommandFromResourceAssembler.toCommandFromResource(resource);
    var administratorId= administratorCommandService.handle(createAdministratorCommand);
    if (administratorId==null|| administratorId==0L){ return ResponseEntity.badRequest().build();}
    var getAdministratorByIdQuery=new GetAdministratorByIdQuery(administratorId);
    var administrator= administratorQueryService.handle(getAdministratorByIdQuery);
    if (administrator.isEmpty()){return ResponseEntity.notFound().build();}
    var administratorEntity=administrator.get();
    var administratorResource= AdministratorResourceFromEntityAssembler.toResourceFromEntity(administratorEntity);
    return new ResponseEntity<>(administratorResource, HttpStatus.CREATED);
  }

  @GetMapping("/{userId}")
  @Operation(summary = "Get administrator by user Id ", description = "Get administrator by user userId")
  @ApiResponses(value= {
    @ApiResponse(responseCode = "201", description = "Administrator found"),
    @ApiResponse(responseCode = "400", description = "Administrator not found ")
  })
  public ResponseEntity<AdministratorResource>getAdministratorByUserId(@PathVariable Long userId){
    var getAdministratorByUerId= administratorQueryService.handle(new GetAdministratorByUserIdQuery(userId));
    if (getAdministratorByUerId.isEmpty()){
      return ResponseEntity.notFound().build();
    }
    var administratorEntity= getAdministratorByUerId.get();
    var administratorResource= AdministratorResourceFromEntityAssembler.toResourceFromEntity(administratorEntity);
    return ResponseEntity.ok(administratorResource);
  }
}
