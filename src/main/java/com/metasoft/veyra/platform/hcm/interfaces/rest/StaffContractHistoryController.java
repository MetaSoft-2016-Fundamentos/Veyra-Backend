package com.metasoft.veyra.platform.hcm.interfaces.rest;

import com.metasoft.veyra.platform.hcm.domain.model.queries.GetActiveContractByStaffMemberIdQuery;
import com.metasoft.veyra.platform.hcm.domain.model.queries.GetAllContractsByStaffMemberIdQuery;
import com.metasoft.veyra.platform.hcm.domain.model.queries.GetContractByStaffMemberIdAndContractIdQuery;
import com.metasoft.veyra.platform.hcm.domain.model.queries.GetLastAddedContractByStaffMemberIdQuery;
import com.metasoft.veyra.platform.hcm.domain.services.StaffCommandServices;
import com.metasoft.veyra.platform.hcm.domain.services.StaffQueryServices;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.AddContractResource;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.ContractResource;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.UpdateContractResource;
import com.metasoft.veyra.platform.hcm.interfaces.rest.transform.AddContractCommandFromResourceAssembler;
import com.metasoft.veyra.platform.hcm.interfaces.rest.transform.ContractResourceFromEntityAssembler;
import com.metasoft.veyra.platform.hcm.interfaces.rest.transform.UpdateContractCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/staff/{staffMemberId}/contracts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Staff")
public class StaffContractHistoryController {

    private final StaffCommandServices staffCommandServices;
    private final StaffQueryServices staffQueryServices;

    public StaffContractHistoryController(StaffCommandServices staffCommandServices, StaffQueryServices staffQueryServices) {
        this.staffCommandServices = staffCommandServices;
        this.staffQueryServices = staffQueryServices;
    }

    @PostMapping
    @Operation(summary = "Add a contract to a staff member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contract added to the staff member"),
            @ApiResponse(responseCode = "404", description = "Bad request")
    })
    public ResponseEntity<ContractResource> addContractToStaff(
            @PathVariable Long staffMemberId,
            @RequestBody AddContractResource resource
    ) {
        var command = AddContractCommandFromResourceAssembler.toCommandFromResource(staffMemberId, resource);
        staffCommandServices.handle(command);

        var getLastContractQuery = new GetLastAddedContractByStaffMemberIdQuery(staffMemberId);
        var contract = staffQueryServices.handle(getLastContractQuery);

        if (contract.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var contractResource = ContractResourceFromEntityAssembler.toResourceFromEntity(contract.get());
        return new ResponseEntity<>(contractResource, HttpStatus.CREATED);
    }

    @PatchMapping("/{contractId}")
    @Operation(summary = "Update contract status", description = "Updates only the status of a contract")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contract status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Staff member or contract not found")
    })
    public ResponseEntity<ContractResource> updateContractStatus(
            @PathVariable Long staffMemberId,
            @PathVariable Long contractId,
            @RequestBody UpdateContractResource resource
    ) {
        var command = UpdateContractCommandFromResourceAssembler.toCommandFromResource(
                staffMemberId,
                contractId,
                resource
        );
        staffCommandServices.handle(command);

        var query = new GetContractByStaffMemberIdAndContractIdQuery(staffMemberId, contractId);
        var contract = staffQueryServices.handle(query);

        if (contract.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var contractResource = ContractResourceFromEntityAssembler.toResourceFromEntity(contract.get());
        return ResponseEntity.ok(contractResource);
    }

    @GetMapping
    @Operation(summary = "Get all contracts of a staff member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contracts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Staff member not found")
    })
    public ResponseEntity<List<ContractResource>> getAllContractsByStaffMemberId(
            @PathVariable Long staffMemberId
    ) {
        var query = new GetAllContractsByStaffMemberIdQuery(staffMemberId);
        var contracts = staffQueryServices.handle(query);

        if (contracts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var contractResources = contracts.stream()
                .map(ContractResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(contractResources);
    }

    @GetMapping("/{contractId}")
    @Operation(
            summary = "Get a specific contract of a staff member",
            description = "Retrieves the details of a specific contract belonging to the given staff member"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contract successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Staff member or contract not found")
    })
    public ResponseEntity<ContractResource> getContractByStaffMemberAndContractId(
            @PathVariable Long staffMemberId,
            @PathVariable Long contractId
    ) {
        var query = new GetContractByStaffMemberIdAndContractIdQuery(staffMemberId, contractId);
        var contract = staffQueryServices.handle(query);

        if (contract.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var contractResource = ContractResourceFromEntityAssembler.toResourceFromEntity(contract.get());
        return ResponseEntity.ok(contractResource);
    }

    @GetMapping("/active")
    @Operation(summary = "Get the active contract of a staff member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active contract retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No active contract found")
    })
    public ResponseEntity<ContractResource> getActiveContract(
            @PathVariable Long staffMemberId
    ) {
        var query = new GetActiveContractByStaffMemberIdQuery(staffMemberId);
        var contract = staffQueryServices.handle(query);

        if (contract.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var contractResource = ContractResourceFromEntityAssembler.toResourceFromEntity(contract.get());
        return ResponseEntity.ok(contractResource);
    }
}