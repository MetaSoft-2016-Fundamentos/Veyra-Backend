package com.metasoft.veyra.platform.health.interfaces.rest;

import com.metasoft.veyra.platform.health.domain.model.queries.GetVitalSignsByResidentIdQuery;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.health.domain.services.VitalSignQueryService;
import com.metasoft.veyra.platform.health.interfaces.rest.resources.VitalSignResource;
import com.metasoft.veyra.platform.health.interfaces.rest.transform.VitalSignResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "/api/v1/resident/{residentId}/vital-signs", produces = APPLICATION_JSON_VALUE)
@RestController
@Tag(name = "Residents")
public class ResidentVitalSignsController {
  private final VitalSignQueryService vitalSignQueryService;

  public ResidentVitalSignsController(VitalSignQueryService vitalSignQueryService) {
    this.vitalSignQueryService = vitalSignQueryService;
  }

  @GetMapping
  @Operation(
    summary = "Get paginated vital signs for a resident with date filtering",
    description = "Returns a paginated list of vital signs within a specific date range for a resident."
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Page of vital signs successfully retrieved."),
    @ApiResponse(responseCode = "400", description = "Invalid criteria provided."),
    @ApiResponse(responseCode = "500", description = "Internal server error.")
  })
  public ResponseEntity<Page<VitalSignResource>> getVitalSignsByResidentId(
    @PathVariable Long residentId,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size) {

    if (endDate == null) {
      endDate = LocalDateTime.now();
    }
    if (startDate == null) {
      startDate = endDate.minusDays(3);
    }

    var pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

    var vitalSignsPage = vitalSignQueryService.handle(
      new GetVitalSignsByResidentIdQuery(new ResidentId(residentId), startDate, endDate, pageable)
    );

    var resourcesPage = vitalSignsPage.map(VitalSignResourceFromEntityAssembler::toResourceFromEntity);

    return ResponseEntity.ok(resourcesPage);
  }
}
