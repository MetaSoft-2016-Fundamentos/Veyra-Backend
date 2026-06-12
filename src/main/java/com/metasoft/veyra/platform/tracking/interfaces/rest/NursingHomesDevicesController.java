package com.metasoft.veyra.platform.tracking.interfaces.rest;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDeviceByIdQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDevicesByNursingHomeIdQuery;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.NursingHomeId;
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
public class NursingHomesDevicesController {

    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;

    public NursingHomesDevicesController(DeviceCommandService deviceCommandService,
                                         DeviceQueryService deviceQueryService) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
    }

    @GetMapping
    @Operation(summary = "Get all tracking devices for a nursing home")
    public ResponseEntity<DevicesListResource> getDevicesByNursingHome(@PathVariable Long nursingHomeId) {
        var nursingHome=new NursingHomeId(nursingHomeId);
        var devices = deviceQueryService.handle(new GetDevicesByNursingHomeIdQuery(nursingHome))
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
        var deviceId = deviceCommandService.handle(command);
        if (deviceId == null || deviceId == 0L) return ResponseEntity.badRequest().build();
        var getDevicesIdQuery= new GetDeviceByIdQuery(deviceId);
        var device= deviceQueryService.handle(getDevicesIdQuery);
        if (device.isEmpty()){return ResponseEntity.notFound().build();}
        var deviceEntity= device.get();
        var deviceResource= DeviceResourceFromEntityAssembler.toResourceFromEntity(deviceEntity);
        return new ResponseEntity<>(deviceResource,HttpStatus.CREATED);
    }
}
