package com.metasoft.veyra.platform.nursing.interfaces.rest;

import com.metasoft.veyra.platform.nursing.domain.model.queries.GetLastAddedRoomByNursingHomeIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetResidentByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetRoomsByStatusAndNursingHomeIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetRoomsForNursingHomeIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.RoomStatus;
import com.metasoft.veyra.platform.nursing.domain.services.NursingHomeCommandServices;
import com.metasoft.veyra.platform.nursing.domain.services.NursingHomeQueryServices;
import com.metasoft.veyra.platform.nursing.domain.services.ResidentCommandServices;
import com.metasoft.veyra.platform.nursing.domain.services.ResidentQueryServices;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.AssignedRoomForResidentResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateRoomResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.ResidentResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.RoomResource;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.AssignedRoomForResidentCommandFromResourceAssembler;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.CreateRoomCommandFromResourceAssembler;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.ResidentResourceFromEntityAssembler;
import com.metasoft.veyra.platform.nursing.interfaces.rest.transform.RoomResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Tag(name = "Nursing Homes")
@RestController
@RequestMapping(value = "/api/v1/nursing-homes/{nursingHomeId}/rooms", produces = APPLICATION_JSON_VALUE)
public class NursingHomeRoomsController {
private final ResidentCommandServices residentCommandServices;
    private final NursingHomeCommandServices nursingHomeCommandServices;
    private final NursingHomeQueryServices nursingHomeQueryServices;
    private final ResidentQueryServices residentQueryServices;

    public NursingHomeRoomsController(ResidentCommandServices residentCommandServices, NursingHomeCommandServices nursingHomeCommandServices,
                                      NursingHomeQueryServices nursingHomeQueryServices, ResidentQueryServices residentQueryServices) {
        this.residentCommandServices = residentCommandServices;
        this.nursingHomeCommandServices = nursingHomeCommandServices;
        this.nursingHomeQueryServices = nursingHomeQueryServices;
        this.residentQueryServices = residentQueryServices;
    }
    @PostMapping
    @Operation(summary = "Add a room to nursing home", description = "Create a new room for the specified nursing home")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Room created for nursing home"),
            @ApiResponse(responseCode = "404", description = "Room not found")
    })
    public ResponseEntity<RoomResource> addRoom(@PathVariable Long nursingHomeId,
                                               @Valid @RequestBody CreateRoomResource resource) {
        var roomCommand = CreateRoomCommandFromResourceAssembler.toCommandFromResource(nursingHomeId, resource);
        nursingHomeCommandServices.handle(roomCommand);

        var getLastRoomQuery = new GetLastAddedRoomByNursingHomeIdQuery(nursingHomeId);
        var room = nursingHomeQueryServices.handle(getLastRoomQuery);

        if (room.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var roomResource = RoomResourceFromEntityAssembler.toResourceFromEntity(room.get());
        return new ResponseEntity<>(roomResource, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all rooms", description = "Get all rooms for the specified nursing home")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully")
    })
    public ResponseEntity<List<RoomResource>> getAllRooms(@PathVariable Long nursingHomeId) {
        var getRoomsQuery = new GetRoomsForNursingHomeIdQuery(nursingHomeId);
        var rooms = nursingHomeQueryServices.handle(getRoomsQuery);

        var roomResources = rooms.stream()
                .map(RoomResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(roomResources);
    }
    @PostMapping("/{residentId}")
    @Operation(summary = "Assign a resident to a room",
            description = "Assign a resident to a specific room in the nursing home")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resident assigned to room successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<ResidentResource> assignResidentToRoom(
            @PathVariable Long nursingHomeId,
            @PathVariable Long residentId,
            @Valid @RequestBody AssignedRoomForResidentResource resource) {

        var command = AssignedRoomForResidentCommandFromResourceAssembler
                .toCommandFromResource(nursingHomeId, residentId, resource);

        residentCommandServices.handle(command);
       var getResidentByIdQuery=residentQueryServices.handle(new GetResidentByIdQuery(residentId));
       if (getResidentByIdQuery.isEmpty()){return ResponseEntity.notFound().build();}
       var residentEntity= getResidentByIdQuery.get();
       var residentResource= ResidentResourceFromEntityAssembler.toResourceFromEntity(residentEntity);
        return ResponseEntity.ok(residentResource);
    }
@GetMapping("/{roomStatus}")
@Operation(summary = "Get rooms by nursing home id and status",description = "Get rooms for a specific nursing home filtered by their status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Rooms retrieved successfully"),
            @ApiResponse(responseCode = "404",description = "Nursing home not found")
    })
    public ResponseEntity<List<RoomResource>> getRoomsByNursingHomeIdAndStatus(@PathVariable String roomStatus, @PathVariable Long nursingHomeId)
    {
        var query= new GetRoomsByStatusAndNursingHomeIdQuery(RoomStatus.valueOf(roomStatus.toUpperCase()),nursingHomeId);
        var rooms= nursingHomeQueryServices.handle(query);
        if (rooms.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var roomResources= rooms.stream().map(RoomResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(roomResources);
  }
}