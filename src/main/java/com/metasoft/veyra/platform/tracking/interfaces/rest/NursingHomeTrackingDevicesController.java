package com.metasoft.veyra.platform.tracking.interfaces.rest;

import com.metasoft.veyra.platform.tracking.domain.model.queries.GetAllDevicesQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDevicesByNursingHomeIdQuery;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceCommandService;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceQueryService;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.DeviceResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.DevicesListResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.RegisterDeviceResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import com.metasoft.veyra.platform.tracking.interfaces.rest.transform.RegisterDeviceCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/nursing-homes/{nursingHomeId}/devices", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Nursing Homes")
public class NursingHomeTrackingDevicesController {

    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;

    public NursingHomeTrackingDevicesController(DeviceCommandService deviceCommandService,
                                                DeviceQueryService deviceQueryService) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
    }

    @GetMapping
    @Operation(summary = "Get all tracking devices for a nursing home")
    public ResponseEntity<DevicesListResource> getDevicesByNursingHome(@PathVariable Long nursingHomeId) {
        var devices = deviceQueryService.handle(new GetDevicesByNursingHomeIdQuery(nursingHomeId))
                .stream()
                .map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(new DevicesListResource(devices));
    }

    @PostMapping
    @Operation(summary = "Register a new tracking device for a nursing home")
    public ResponseEntity<DeviceResource> registerDevice(@PathVariable Long nursingHomeId,
                                                         @Valid @RequestBody RegisterDeviceResource resource) {
        var command = RegisterDeviceCommandFromResourceAssembler.toCommandFromResource(resource, nursingHomeId);
        var numericId = deviceCommandService.handle(command);
        if (numericId == null || numericId == 0L) return ResponseEntity.badRequest().build();
        var device = deviceQueryService.handle(new GetAllDevicesQuery())
                .stream().filter(d -> d.getId().equals(numericId)).findFirst();
        if (device.isEmpty()) return ResponseEntity.notFound().build();
        return new ResponseEntity<>(DeviceResourceFromEntityAssembler.toResourceFromEntity(device.get()), HttpStatus.CREATED);
    }
}
