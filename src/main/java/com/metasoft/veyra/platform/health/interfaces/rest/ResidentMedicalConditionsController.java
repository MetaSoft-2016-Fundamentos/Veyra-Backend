package com.metasoft.veyra.platform.health.interfaces.rest;

import com.metasoft.veyra.platform.health.domain.services.MedicalConditionCommandService;
import com.metasoft.veyra.platform.health.domain.services.MedicalConditionQueryService;
import com.metasoft.veyra.platform.health.domain.model.aggregates.MedicalCondition;
import com.metasoft.veyra.platform.health.domain.model.commands.RegisterMedicalConditionCommand;
import com.metasoft.veyra.platform.health.domain.model.queries.GetMedicalConditionsByResidentIdQuery;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.health.interfaces.rest.resources.MedicalConditionResource;
import com.metasoft.veyra.platform.health.interfaces.rest.resources.RegisterMedicalConditionResource;
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
@RequestMapping(value = "/api/v1/residents/{residentId}/medical-conditions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Residents")
public class ResidentMedicalConditionsController {

  private final MedicalConditionCommandService commandService;
  private final MedicalConditionQueryService queryService;

  public ResidentMedicalConditionsController(MedicalConditionCommandService commandService, MedicalConditionQueryService queryService) {
    this.commandService = commandService;
    this.queryService = queryService;
  }

  @PostMapping
  @Operation(summary = "Register a new medical condition for a resident")
  @Parameter(name = "residentId", description = "Identifier of the resident", required = true)
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Medical condition registered"),
    @ApiResponse(responseCode = "400", description = "Bad Request")
  })
  public ResponseEntity<MedicalConditionResource> registerMedicalCondition(
    @Valid @RequestBody RegisterMedicalConditionResource resource,
    @PathVariable Long residentId) {

    var command = new RegisterMedicalConditionCommand(
      residentId,
      resource.diagnosisName(),
      resource.diagnosisDate(),
      resource.status(),
      resource.notes()
    );

    var conditionId = commandService.handle(command);
    if (conditionId == null || conditionId == 0L) {
      return ResponseEntity.badRequest().build();
    }

    var conditionOpt = queryService.handle(conditionId);
    return conditionOpt.map(medicalCondition -> new ResponseEntity<>(toResource(medicalCondition), HttpStatus.CREATED)).orElseGet(() -> ResponseEntity.notFound().build());

  }

  @GetMapping
  @Operation(summary = "Get all medical conditions for a resident")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Medical conditions successfully retrieved")
  })
  public ResponseEntity<List<MedicalConditionResource>> getAllMedicalConditions(@PathVariable Long residentId) {
    var query = new GetMedicalConditionsByResidentIdQuery(new ResidentId(residentId));
    var conditions = queryService.handle(query);

    var resources = conditions.stream()
      .map(this::toResource)
      .toList();

    return ResponseEntity.ok(resources);
  }

  private MedicalConditionResource toResource(MedicalCondition entity) {
    return new MedicalConditionResource(
      entity.getId(),
      entity.getResidentId().residentId(),
      entity.getDiagnosisName(),
      entity.getDiagnosisDate(),
      entity.getStatus().name(),
      entity.getNotes()
    );
  }
}
