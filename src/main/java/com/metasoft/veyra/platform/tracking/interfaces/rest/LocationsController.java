package com.metasoft.veyra.platform.tracking.interfaces.rest;

import com.metasoft.veyra.platform.tracking.domain.services.LocationCommandService;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.LocationResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.RecordLocationResource;
import com.metasoft.veyra.platform.tracking.interfaces.rest.transform.LocationResourceFromEntityAssembler;
import com.metasoft.veyra.platform.tracking.interfaces.rest.transform.RecordLocationCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/locations", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Locations", description = "GPS Location Recording")
public class LocationsController {

    private final LocationCommandService locationCommandService;

    public LocationsController(LocationCommandService locationCommandService) {
        this.locationCommandService = locationCommandService;
    }

    @PostMapping
    @Operation(summary = "Record a GPS location for a device")
    public ResponseEntity<LocationResource> recordLocation(@Valid @RequestBody RecordLocationResource resource) {
        var command = RecordLocationCommandFromResourceAssembler.toCommandFromResource(resource);
        var location = locationCommandService.handle(command);
        return new ResponseEntity<>(LocationResourceFromEntityAssembler.toResourceFromEntity(location), HttpStatus.CREATED);
    }
}
