package com.metasoft.veyra.platform.communication.interfaces.rest;

import com.metasoft.veyra.platform.communication.domain.model.queries.GetUserPushTokensQuery;
import com.metasoft.veyra.platform.communication.domain.services.UserPushTokenCommandService;
import com.metasoft.veyra.platform.communication.domain.services.UserPushTokenQueryService;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.RegisterUserPushTokenResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.UnregisterUserPushTokenResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.UserPushTokenResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.transform.UserPushTokenAssembler;
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

@RestController
@RequestMapping(value = "/api/v1/users/{userId}/push-tokens", produces = APPLICATION_JSON_VALUE)
@Tag(name = "User Push Tokens", description = "Register FCM tokens for mobile and web app users")
public class UserPushTokensController {

    private final UserPushTokenCommandService userPushTokenCommandService;
    private final UserPushTokenQueryService userPushTokenQueryService;

    public UserPushTokensController(
            UserPushTokenCommandService userPushTokenCommandService,
            UserPushTokenQueryService userPushTokenQueryService
    ) {
        this.userPushTokenCommandService = userPushTokenCommandService;
        this.userPushTokenQueryService = userPushTokenQueryService;
    }

    @GetMapping
    @Operation(summary = "List user push tokens", description = "Returns all FCM tokens registered for the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tokens retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<UserPushTokenResource>> getUserPushTokens(@PathVariable Long userId) {
        var tokens = userPushTokenQueryService.handle(new GetUserPushTokensQuery(userId));
        var resources = tokens.stream().map(UserPushTokenAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    @Operation(summary = "Register push token", description = "Registers or refreshes an FCM token for the user (Android, iOS, or Web).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Token registered"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserPushTokenResource> registerPushToken(
            @PathVariable Long userId,
            @Valid @RequestBody RegisterUserPushTokenResource resource
    ) {
        var command = UserPushTokenAssembler.toRegisterCommand(userId, resource);
        var token = userPushTokenCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserPushTokenAssembler.toResource(token));
    }

    @DeleteMapping
    @Operation(summary = "Unregister push token", description = "Removes an FCM token when the user logs out or disables notifications.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Token removed"),
            @ApiResponse(responseCode = "404", description = "User or token not found")
    })
    public ResponseEntity<Void> unregisterPushToken(
            @PathVariable Long userId,
            @Valid @RequestBody UnregisterUserPushTokenResource resource
    ) {
        var command = UserPushTokenAssembler.toUnregisterCommand(userId, resource);
        userPushTokenCommandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}
