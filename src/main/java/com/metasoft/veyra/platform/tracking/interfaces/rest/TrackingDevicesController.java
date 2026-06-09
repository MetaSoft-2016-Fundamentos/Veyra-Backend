package com.metasoft.veyra.platform.tracking.interfaces.rest;

import com.metasoft.veyra.platform.tracking.domain.model.commands.DeleteDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetAllDevicesQuery;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceCommandService;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceQueryService;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.DeviceResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.RegisterDeviceResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import com.metasoft.veyra.platform.tracking.interfaces.rest.transform.UpdateDeviceCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/devices", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Tracking Devices", description = "IoT Device Management")
public class TrackingDevicesController {

    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;

    public TrackingDevicesController(DeviceCommandService deviceCommandService,
                                     DeviceQueryService deviceQueryService) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update the type of a tracking device")
    public ResponseEntity<DeviceResource> updateDevice(@PathVariable Long id,
                                                       @Valid @RequestBody RegisterDeviceResource resource) {
        var command = UpdateDeviceCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedId = deviceCommandService.handle(command);
        var device = deviceQueryService.handle(new GetAllDevicesQuery())
                .stream().filter(d -> d.getId().equals(updatedId)).findFirst();
        if (device.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device.get()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tracking device")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceCommandService.handle(new DeleteDeviceCommand(id));
        return ResponseEntity.ok().build();
    }
}
