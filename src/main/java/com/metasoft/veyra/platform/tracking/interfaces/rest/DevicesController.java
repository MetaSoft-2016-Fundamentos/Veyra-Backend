package com.metasoft.veyra.platform.tracking.interfaces.rest;

import com.metasoft.veyra.platform.tracking.domain.model.commands.UnassignDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetAllDevicesQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDeviceByIdQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetUnassignedDevicesQuery;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceCommandService;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceQueryService;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.AssignDeviceResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.DeviceResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.transform.AssignDeviceCommandFromResourceAssembler;
import com.metasoft.veyra.platform.tracking.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping(value = "/api/v1/devices", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Devices", description = "IoT Device Management")
public class DevicesController {
    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;
    public DevicesController(
            DeviceCommandService deviceCommandService,
            DeviceQueryService deviceQueryService) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
    }
    @GetMapping
    @Operation(summary = "Get all devices")
    public ResponseEntity<List<DeviceResource>> getAllDevices() {
        var devices = deviceQueryService.handle(new GetAllDevicesQuery());
        var resources = devices.stream()
                .map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
    @GetMapping("/{deviceId}")
    @Operation(summary = "Get device by ID")
    public ResponseEntity<DeviceResource> getDeviceById(@PathVariable String deviceId) {
        var device = deviceQueryService.handle(new GetDeviceByIdQuery(deviceId));
        return device.map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/unassigned")
    @Operation(summary = "Get all unassigned devices")
    public ResponseEntity<List<DeviceResource>> getUnassignedDevices() {
        var devices = deviceQueryService.handle(new GetUnassignedDevicesQuery());
        var resources = devices.stream()
                .map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
    @Hidden
    @PostMapping("/{deviceId}/assignments")
    @Operation(summary = "Assign device to resident")
    public ResponseEntity<DeviceResource> assignDevice(
            @PathVariable String deviceId,
          @Valid @RequestBody AssignDeviceResource resource) {

        var command = AssignDeviceCommandFromResourceAssembler.toCommandFromResource(deviceId, resource);
        deviceCommandService.handle(command);

        var device = deviceQueryService.handle(new GetDeviceByIdQuery(deviceId));
        return device.map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> ResponseEntity.status(HttpStatus.OK).body(r))
                .orElse(ResponseEntity.notFound().build());
    }
    @Hidden
    @PostMapping("/{deviceId}/unassigns")
    @Operation(summary = "Unassign device from resident")
    public ResponseEntity<Void> unassignDevice(@PathVariable String deviceId) {
        var command = new UnassignDeviceCommand(deviceId);
        deviceCommandService.handle(command);
        return ResponseEntity.ok().build();
    }
}