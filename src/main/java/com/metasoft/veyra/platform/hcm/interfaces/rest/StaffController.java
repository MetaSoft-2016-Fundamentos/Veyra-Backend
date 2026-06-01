package com.metasoft.veyra.platform.hcm.interfaces.rest;

import com.metasoft.veyra.platform.hcm.domain.services.StaffCommandServices;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.StaffResource;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.UpdateStaffResource;
import com.metasoft.veyra.platform.hcm.interfaces.rest.transform.StaffResourceFromEntityAssembler;
import com.metasoft.veyra.platform.hcm.interfaces.rest.transform.UpdateStaffCommandFromAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/staff",produces = APPLICATION_JSON_VALUE)
@Tag(name = "Staff", description = "Available endpoints ")
public class StaffController {
private final StaffCommandServices staffCommandServices;

    public StaffController(StaffCommandServices staffCommandServices) {
        this.staffCommandServices = staffCommandServices;
    }


    @PutMapping("/{staffMemberId}")
    @Operation(summary = " Staff member updated by ID",description = "Staff member updated by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = " staff member  update"),
            @ApiResponse(responseCode = "404",description = "staff member not found")
    })
    @Parameter(name = "staffMemberId",description = " The unique identifier of the staff member",required = true)
    public ResponseEntity<StaffResource> updateStaff( @RequestBody UpdateStaffResource resource, @PathVariable Long staffMemberId){
        var updatestaffCommand= UpdateStaffCommandFromAssembler.toCommandFromResource(staffMemberId,resource);
        var staffUpdate=staffCommandServices.handle(updatestaffCommand);
        if (staffUpdate.isEmpty()){return ResponseEntity.badRequest().build();}
        var staffEntity= staffUpdate.get();
        var staffResource= StaffResourceFromEntityAssembler.toResourceFromEntity(staffEntity);
        return ResponseEntity.ok(staffResource);

    }

}
