package com.metasoft.veyra.platform.hcm.interfaces.rest;

import com.metasoft.veyra.platform.hcm.domain.model.queries.GetNursingHomeByStaffIdQuery;
import com.metasoft.veyra.platform.hcm.domain.model.queries.GetStaffByUserIdQuery;
import com.metasoft.veyra.platform.hcm.domain.services.StaffCommandServices;
import com.metasoft.veyra.platform.hcm.domain.services.StaffQueryServices;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.NursingHomeStaffResource;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.StaffResource;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.UpdateStaffResource;
import com.metasoft.veyra.platform.hcm.interfaces.rest.transform.StaffResourceFromEntityAssembler;
import com.metasoft.veyra.platform.hcm.interfaces.rest.transform.UpdateStaffCommandFromAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/staff",produces = APPLICATION_JSON_VALUE)
@Tag(name = "Staff", description = "Available endpoints ")
public class StaffController {
    private final StaffCommandServices staffCommandServices;
    private final StaffQueryServices staffQueryServices;

    public StaffController(StaffCommandServices staffCommandServices, StaffQueryServices staffQueryServices) {
        this.staffCommandServices = staffCommandServices;
        this.staffQueryServices = staffQueryServices;
    }


    @PutMapping(value = "/{staffMemberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = " Staff member updated by ID",description = "Staff member updated by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = " staff member  update"),
            @ApiResponse(responseCode = "404",description = "staff member not found")
    })
    @Parameter(name = "staffMemberId",description = " The unique identifier of the staff member",required = true)
    public ResponseEntity<StaffResource> updateStaff(@Valid @ModelAttribute UpdateStaffResource resource, @PathVariable Long staffMemberId){
        var updatestaffCommand= UpdateStaffCommandFromAssembler.toCommandFromResource(staffMemberId,resource);
        var staffUpdate=staffCommandServices.handle(updatestaffCommand);
        if (staffUpdate.isEmpty()){return ResponseEntity.badRequest().build();}
        var staffEntity= staffUpdate.get();
        var staffResource= StaffResourceFromEntityAssembler.toResourceFromEntity(staffEntity);
        return ResponseEntity.ok(staffResource);

    }

    @GetMapping("/{staffMemberId}/nursing-homes")
    @Operation(summary = "Get nursing home by staff member ID", description = "Get the nursing home associated with a staff member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nursing home found"),
            @ApiResponse(responseCode = "404", description = "Nursing home not found for the given staff member")
    })
    public ResponseEntity<NursingHomeStaffResource> getNursingHomeByStaffId(@PathVariable Long staffMemberId) {
        var getNursingHomeByStaffIdQuery = new GetNursingHomeByStaffIdQuery(staffMemberId);
        var nursingHomeId = staffQueryServices.handle(getNursingHomeByStaffIdQuery);
        return nursingHomeId.map(id -> new NursingHomeStaffResource(id, staffMemberId))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-user/{userId}/nursing-homes")
    @Operation(summary = "Get nursing home by user ID", description = "Get the nursing home associated with a user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nursing home found"),
            @ApiResponse(responseCode = "404", description = "Nursing home not found for the given user ID")
    })
    public ResponseEntity<NursingHomeStaffResource> getNursingHomeByUserId(@PathVariable Long userId) {
        var getStaffByUserIdQuery = new GetStaffByUserIdQuery(userId);
        var staff = staffQueryServices.handle(getStaffByUserIdQuery);
        return staff.filter(s -> s.getNursingHomeId() != null)
                .map(s -> new NursingHomeStaffResource(s.getNursingHomeId().nursingHomeId(), s.getId()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}