package com.metasoft.veyra.platform.tracking.interfaces.rest;

import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDevicesByResidentIdQuery;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceQueryService;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.DeviceResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/residents/{residentId}/devices",produces = APPLICATION_JSON_VALUE)
@Tag(name="Residents")
public class ResidentDeviceController {
    private final DeviceQueryService deviceQueryService;

    public ResidentDeviceController(DeviceQueryService deviceQueryService) {
        this.deviceQueryService = deviceQueryService;
    }

    @GetMapping
    @Operation(summary = "Get devices by resident")
    public ResponseEntity<List<DeviceResource>> getDevicesByResident(@PathVariable Long residentId) {
        var devices = deviceQueryService.handle(new GetDevicesByResidentIdQuery(new ResidentId(residentId)));
        var resources = devices.stream()
                .map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
}
