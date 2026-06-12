package com.metasoft.veyra.platform.tracking.interfaces.rest;
import com.metasoft.veyra.platform.tracking.domain.model.commands.AssignDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.ChangeDeviceStatusCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.UnassignDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetAllDevicesQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDeviceByIdQuery;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceCommandService;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceQueryService;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.AssignDeviceResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.ChangeDeviceStatusResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.DeviceResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.RegisterDeviceResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import com.metasoft.veyra.platform.tracking.interfaces.rest.transform.UpdateDeviceCommandFromResourceAssembler;
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
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/devices",  produces = APPLICATION_JSON_VALUE)
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
    @Operation(summary = "Get device by id")
    public ResponseEntity<DeviceResource> getDeviceById(@PathVariable Long deviceId) {
        var device = deviceQueryService.handle(new GetDeviceByIdQuery(deviceId));
        return device.map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/{deviceId}/assignments")
    @Operation(summary = "Assign device to resident")
    public ResponseEntity<DeviceResource> assignDevice(@PathVariable Long deviceId,
                                                       @Valid @RequestBody AssignDeviceResource resource) {
        var device = deviceQueryService.handle(new GetAllDevicesQuery()).stream()
                .filter(d -> d.getId().equals(deviceId)).findFirst()
                .orElse(null);
        if (device == null) return ResponseEntity.notFound().build();

        var command = new AssignDeviceCommand(device.getId(), resource.residentId());
        deviceCommandService.handle(command);

        var updated = deviceQueryService.handle(new GetAllDevicesQuery()).stream()
                .filter(d -> d.getId().equals(deviceId)).findFirst();
        return updated.map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> ResponseEntity.status(HttpStatus.OK).body(r))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{deviceId}/assignments")
    @Operation(summary = "Unassign device from resident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "unassign device"),
            @ApiResponse(responseCode = "404", description = "device not found")})

    public ResponseEntity<?> unassignDevice(@PathVariable             @Parameter(description = "Unique course identifier", example = "1", required = true)
                                                Long deviceId
    ) {
        var unassignDeviceCommand= new UnassignDeviceCommand(deviceId);
        deviceCommandService.handle(unassignDeviceCommand);
        return ResponseEntity.ok(
                Map.of("message", "Device successfully unassigned")
        );
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

    @PutMapping("/{deviceId}")
    @Operation(summary = "Update the type of a tracking device")
    public ResponseEntity<DeviceResource> updateDevice(@PathVariable Long deviceId,
                                                       @Valid @RequestBody RegisterDeviceResource resource) {
        var command = UpdateDeviceCommandFromResourceAssembler.toCommandFromResource(deviceId, resource);
        var updatedId = deviceCommandService.handle(command);
        var device = deviceQueryService.handle(new GetAllDevicesQuery())
                .stream().filter(d -> d.getId().equals(updatedId)).findFirst();
        if (device.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device.get()));
    }
}
