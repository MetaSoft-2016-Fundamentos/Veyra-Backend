package com.metasoft.veyra.platform.tracking.interfaces.rest;

import com.metasoft.veyra.platform.tracking.domain.model.commands.AssignDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.ChangeDeviceStatusCommand;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetAllDevicesQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDeviceByIdQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetUnassignedDevicesQuery;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceCommandService;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceQueryService;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.AssignDeviceResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.ChangeDeviceStatusResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.DeviceResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
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

    public DevicesController(DeviceCommandService deviceCommandService,
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
    @Operation(summary = "Get device by MAC address")
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

    @PostMapping("/{deviceId}/assignments")
    @Operation(summary = "Assign device to resident")
    public ResponseEntity<DeviceResource> assignDevice(@PathVariable Long deviceId,
                                                       @Valid @RequestBody AssignDeviceResource resource) {
        var device = deviceQueryService.handle(new GetAllDevicesQuery()).stream()
                .filter(d -> d.getId().equals(deviceId)).findFirst()
                .orElse(null);
        if (device == null) return ResponseEntity.notFound().build();

        var command = new AssignDeviceCommand(device.getDeviceId(), resource.residentId(), resource.assignedBy());
        deviceCommandService.handle(command);

        var updated = deviceQueryService.handle(new GetAllDevicesQuery()).stream()
                .filter(d -> d.getId().equals(deviceId)).findFirst();
        return updated.map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> ResponseEntity.status(HttpStatus.OK).body(r))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{deviceId}/assignments")
    @Operation(summary = "Unassign device from resident")
    public ResponseEntity<Void> unassignDevice(@PathVariable Long deviceId) {
        var device = deviceQueryService.handle(new GetAllDevicesQuery()).stream()
                .filter(d -> d.getId().equals(deviceId)).findFirst()
                .orElse(null);
        if (device == null) return ResponseEntity.notFound().build();

        deviceCommandService.handle(new com.metasoft.veyra.platform.tracking.domain.model.commands.UnassignDeviceCommand(device.getDeviceId()));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{deviceId}/status")
    @Operation(summary = "Change device status")
    public ResponseEntity<DeviceResource> changeDeviceStatus(@PathVariable Long deviceId,
                                                              @Valid @RequestBody ChangeDeviceStatusResource resource) {
        var updatedId = deviceCommandService.handle(new ChangeDeviceStatusCommand(deviceId, resource.status()));
        var updated = deviceQueryService.handle(new GetAllDevicesQuery()).stream()
                .filter(d -> d.getId().equals(updatedId)).findFirst();
        return updated.map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
