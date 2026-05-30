package com.metasoft.veyra.platform.communication.interfaces.rest;

import com.metasoft.veyra.platform.communication.domain.services.PushNotificationCommandService;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.CommunicationAcceptedResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.SendPushNotificationResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.transform.SendPushNotificationCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/push-notifications", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Push Notifications", description = "Available Push Notification Endpoints")
public class PushNotificationsController {

    private final PushNotificationCommandService pushNotificationCommandService;

    public PushNotificationsController(PushNotificationCommandService pushNotificationCommandService) {
        this.pushNotificationCommandService = pushNotificationCommandService;
    }

    @PostMapping
    @Operation(summary = "Send push notification", description = "Send a push notification to a device token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Push notification accepted for delivery"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "502", description = "Push provider error"),
            @ApiResponse(responseCode = "503", description = "Push provider not configured")
    })
    public ResponseEntity<CommunicationAcceptedResource> sendPushNotification(
            @Valid @RequestBody SendPushNotificationResource resource
    ) {
        var command = SendPushNotificationCommandFromResourceAssembler.toCommandFromResource(resource);
        pushNotificationCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CommunicationAcceptedResource("Push notification accepted for delivery"));
    }
}
