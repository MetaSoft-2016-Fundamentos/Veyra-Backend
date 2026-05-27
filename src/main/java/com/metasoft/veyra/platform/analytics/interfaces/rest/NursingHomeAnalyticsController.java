package com.metasoft.veyra.platform.analytics.interfaces.rest;

import com.metasoft.veyra.platform.analytics.domain.model.queries.GetResidentAdmissionsByNursingHomeIdAndYearQuery;
import com.metasoft.veyra.platform.analytics.domain.model.queries.GetStaffHiresByNursingHomeIdAndYearQuery;
import com.metasoft.veyra.platform.analytics.domain.model.queries.GetStaffTerminationsByNursingHomeIdAndYearQuery;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.analytics.domain.services.MetricQueryService;
import com.metasoft.veyra.platform.analytics.interfaces.rest.resources.MetricResource;
import com.metasoft.veyra.platform.analytics.interfaces.rest.transform.MetricResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/nursing-homes/{nursingHomeId}/analytics", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Nursing Homes")
public class NursingHomeAnalyticsController {

    private final MetricQueryService metricQueryService;

    public NursingHomeAnalyticsController(MetricQueryService metricQueryService) {
        this.metricQueryService = metricQueryService;
    }

    @GetMapping("/residents-admissions")
    @Operation(
            summary = "Get resident admissions analytics",
            description = "Returns aggregated analytics for resident admissions ready for charts"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analytics returned successfully"),
            @ApiResponse(responseCode = "404", description = "No metrics found")
    })
    public ResponseEntity<MetricResource> getResidentAdmissionsAnalytics(
            @PathVariable Long nursingHomeId,
            @RequestParam int year) {

        var nursingHomeIdVO = new NursingHomeId(nursingHomeId);
        var query = new GetResidentAdmissionsByNursingHomeIdAndYearQuery(nursingHomeIdVO, year);
        var metrics = metricQueryService.handle(query);

        if (metrics.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resource = MetricResourceFromEntityAssembler.toResourceFromEntityList(metrics);
        return ResponseEntity.ok(resource);
    }
    @GetMapping("/staff-hires")
    @Operation(
            summary = "Get staff hires analytics",
            description = "Returns analytics for staff hires"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analytics returned successfully"),
            @ApiResponse(responseCode = "404", description = "No metrics found")
    })
    public ResponseEntity<MetricResource> getStaffHiresAnalytics(
            @PathVariable Long nursingHomeId,
            @RequestParam int year) {

        var nursingHomeIdVO = new NursingHomeId(nursingHomeId);
        var query = new GetStaffHiresByNursingHomeIdAndYearQuery(nursingHomeIdVO, year);
        var metrics = metricQueryService.handle(query);

        if (metrics.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resource = MetricResourceFromEntityAssembler.toResourceFromEntityList(metrics);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/staff-terminations")
    @Operation(
            summary = "Get staff terminations analytics",
            description = "Returns analytics for staff terminations"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analytics returned successfully"),
            @ApiResponse(responseCode = "404", description = "No metrics found")
    })
    public ResponseEntity<MetricResource> getStaffTerminationsAnalytics(
            @PathVariable Long nursingHomeId,
            @RequestParam int year) {

        var nursingHomeIdVO = new NursingHomeId(nursingHomeId);
        var query = new GetStaffTerminationsByNursingHomeIdAndYearQuery(nursingHomeIdVO, year);
        var metrics = metricQueryService.handle(query);

        if (metrics.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resource = MetricResourceFromEntityAssembler.toResourceFromEntityList(metrics);
        return ResponseEntity.ok(resource);
    }
}