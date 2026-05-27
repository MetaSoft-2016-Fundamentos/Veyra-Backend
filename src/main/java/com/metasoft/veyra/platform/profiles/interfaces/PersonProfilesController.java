package com.metasoft.veyra.platform.profiles.interfaces;

import com.metasoft.veyra.platform.profiles.domain.model.commands.DeletePersonProfileCommand;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetAllPersonProfileQuery;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetPersonProfileByIdQuery;
import com.metasoft.veyra.platform.profiles.domain.services.PersonProfileCommandService;
import com.metasoft.veyra.platform.profiles.domain.services.PersonProfileQueryService;
import com.metasoft.veyra.platform.profiles.interfaces.rest.resources.CreatePersonProfileResource;
import com.metasoft.veyra.platform.profiles.interfaces.rest.resources.PersonProfileResource;
import com.metasoft.veyra.platform.profiles.interfaces.rest.resources.UpdatePersonProfileResource;
import com.metasoft.veyra.platform.profiles.interfaces.rest.transform.CreatePersonProfileCommandFromResourceAssembler;
import com.metasoft.veyra.platform.profiles.interfaces.rest.transform.PersonProfileResourceFromEntityAssembler;
import com.metasoft.veyra.platform.profiles.interfaces.rest.transform.UpdatePersonProfileCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/v1/person-profiles",produces = APPLICATION_JSON_VALUE)
@Tag(name = "Person profiles ", description = "Available Person profiles Endpoints")
public class PersonProfilesController {
    private final PersonProfileCommandService personProfileCommandService;
    private final PersonProfileQueryService personProfileQueryService;

    public PersonProfilesController(PersonProfileCommandService personProfileCommandService, PersonProfileQueryService personProfileQueryService) {
        this.personProfileCommandService = personProfileCommandService;
        this.personProfileQueryService = personProfileQueryService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create  a new person profile ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person profile created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<PersonProfileResource> createPersonProfile( @Valid @ModelAttribute CreatePersonProfileResource resource) {
        var createPersonProfileCommand = CreatePersonProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var personProfile = personProfileCommandService.handle(createPersonProfileCommand);
        if (personProfile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var createdPerson = personProfile.get();
        var personProfileResource = PersonProfileResourceFromEntityAssembler.toResourceFromEntity(createdPerson);
        return new ResponseEntity<>(personProfileResource, HttpStatus.CREATED);
    }

    @GetMapping("/{personProfileId}")
    @Operation(summary = "Get person profile by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person profile found "),
            @ApiResponse(responseCode = "404", description = "Person profile not found ")
    })
    @Parameter(name = "personProfileId",description = " The unique identifier of the person profile",required = true)
    public ResponseEntity<PersonProfileResource> getPersonProfileById(@PathVariable Long personProfileId) {
        var getPersonProfileQuery = personProfileQueryService.handle(new GetPersonProfileByIdQuery(personProfileId));
        if (getPersonProfileQuery.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var personProfileEntity = getPersonProfileQuery.get();
        var personProfileResource = PersonProfileResourceFromEntityAssembler.toResourceFromEntity(personProfileEntity);
        return ResponseEntity.ok(personProfileResource);
    }
    @GetMapping()
    @Operation(summary = "Get all person profiles",description = "delete person profile ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person profiles found "),
            @ApiResponse(responseCode = "400",description = " Person profiles not found ")})
    public ResponseEntity<List<PersonProfileResource>>getAllPersonProfiles() {
        var getAllPersonProfileQuery=personProfileQueryService.handle(new GetAllPersonProfileQuery());
        if (getAllPersonProfileQuery.isEmpty()){return ResponseEntity.notFound().build();}
        var personProfileResource= getAllPersonProfileQuery.stream().map(PersonProfileResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(personProfileResource);
    }
    @DeleteMapping("/{personProfileId}")
    @Operation(summary = "Person profile delete by id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person profile delete "),
            @ApiResponse(responseCode = "404",description = "Person profile not found ")
    })
    @Parameter(name = "personProfileId",description = "The unique identifier of the person profile", required = true)
    public ResponseEntity<?>deletePersonProfileById(@PathVariable Long personProfileId) {
        var deletePersonProfileCommand= new DeletePersonProfileCommand(personProfileId);
        personProfileCommandService.handle(deletePersonProfileCommand);
        return ResponseEntity.ok("Person profile with give id successfully deleted");
    }

    @PutMapping("/{personProfileId}")
    @Operation(summary = "Person profile updated by id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person profile updated "),
            @ApiResponse(responseCode = "404",description = "Person profile not found ")
    })
    @Parameter(name = "personProfileId",description = "The unique identifier of the person profile", required = true)
    public ResponseEntity<PersonProfileResource>updatedPersonProfile(@PathVariable Long personProfileId,@RequestBody UpdatePersonProfileResource resource)
    {
        var updatedPersonProfileCommand= UpdatePersonProfileCommandFromResourceAssembler.toCommandFromResource(personProfileId,resource);
        var updatedPersonProfile= personProfileCommandService.handle(updatedPersonProfileCommand);
        if (updatedPersonProfile.isEmpty()) {return ResponseEntity.notFound().build();}
        var updatedPersonProfileEntity= updatedPersonProfile.get();
        var updatePersonProfileResource= PersonProfileResourceFromEntityAssembler.toResourceFromEntity(updatedPersonProfileEntity);
        return ResponseEntity.ok(updatePersonProfileResource);
    }
}
