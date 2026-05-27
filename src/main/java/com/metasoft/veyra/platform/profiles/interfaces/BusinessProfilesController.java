package com.metasoft.veyra.platform.profiles.interfaces;

import com.metasoft.veyra.platform.profiles.domain.model.queries.GetAllBusinessProfileQuery;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetBusinessProfileByIdQuery;
import com.metasoft.veyra.platform.profiles.domain.services.BusinessProfileCommandService;
import com.metasoft.veyra.platform.profiles.domain.services.BusinessProfileQueryService;
import com.metasoft.veyra.platform.profiles.interfaces.rest.resources.BusinessProfileResource;
import com.metasoft.veyra.platform.profiles.interfaces.rest.resources.CreateBusinessProfileResource;
import com.metasoft.veyra.platform.profiles.interfaces.rest.transform.BusinessProfileResourceFromEntityAssembler;
import com.metasoft.veyra.platform.profiles.interfaces.rest.transform.CreateBusinessProfileCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/v1/business-profiles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Business Profiles",description = "Available Business Profile Endpoints")
public class BusinessProfilesController {
    private final BusinessProfileCommandService businessProfileCommandService;
    private final BusinessProfileQueryService businessProfileQueryService;

    public BusinessProfilesController(BusinessProfileCommandService businessProfileCommandService, BusinessProfileQueryService businessProfileQueryService) {
        this.businessProfileCommandService = businessProfileCommandService;
        this.businessProfileQueryService = businessProfileQueryService;
            }


        @PostMapping
    @Operation(summary = "Create a new business profile")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Business profile created"),
                    @ApiResponse(responseCode = "400", description = "Bad request ")
            })
        public ResponseEntity<BusinessProfileResource> createBusinessProfile(@RequestBody CreateBusinessProfileResource resource)
        {
        var createBusinessProfileCommand= CreateBusinessProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var businessProfile= businessProfileCommandService.handle(createBusinessProfileCommand);
        if (businessProfile.isEmpty()){return ResponseEntity.badRequest().build();}
        var createdBusinessProfile= businessProfile.get();
        var businessProfileResource= BusinessProfileResourceFromEntityAssembler.toResourceFromEntity(createdBusinessProfile);
        return new ResponseEntity<>(businessProfileResource, HttpStatus.CREATED);
        }

        @GetMapping("/{businessId}")
        @Operation(summary = "Get business profile by Id")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200",description = "Business Profile found"),
                @ApiResponse(responseCode = "404",description = "Business Profile not found ")
        })
        @Parameter(name = "businessId", description = " The unique identifier of the business profile",required = true)
        public ResponseEntity<BusinessProfileResource>getBusinessProfileById(@PathVariable Long businessId){
        var businessProfile= businessProfileQueryService.handle(new GetBusinessProfileByIdQuery(businessId));
        if (businessProfile.isEmpty()){return ResponseEntity.notFound().build();}
        var businessProfileEntity=businessProfile.get();
        var businessProfileResource=BusinessProfileResourceFromEntityAssembler.toResourceFromEntity(businessProfileEntity);
        return ResponseEntity.ok(businessProfileResource);
        }
        @GetMapping
    @Operation(summary = "Get all business profiles ")
      @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "Business profiles found "),
              @ApiResponse(responseCode = "404", description = "No business profiles found")
      })
    public ResponseEntity<List<BusinessProfileResource>>getAllBusinessProfile(){
        var businessProfile= businessProfileQueryService.handle(new GetAllBusinessProfileQuery());
        if(businessProfile.isEmpty()){return ResponseEntity.notFound().build();}
        var businessProfileResource=businessProfile.stream().map(BusinessProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(businessProfileResource);
        }


}
