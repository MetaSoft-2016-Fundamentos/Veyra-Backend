package com.metasoft.veyra.platform.communication.interfaces.rest;

import com.metasoft.veyra.platform.communication.domain.services.EmailNotificationCommandService;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.CommunicationAcceptedResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.SendHtmlEmailResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.SendPlainEmailResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.SendRenderedTemplateEmailResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.SendTemplateEmailResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.transform.SendHtmlEmailCommandFromResourceAssembler;
import com.metasoft.veyra.platform.communication.interfaces.rest.transform.SendPlainEmailCommandFromResourceAssembler;
import com.metasoft.veyra.platform.communication.interfaces.rest.transform.SendRenderedTemplateEmailCommandFromResourceAssembler;
import com.metasoft.veyra.platform.communication.interfaces.rest.transform.SendTemplateEmailCommandFromResourceAssembler;
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
@RequestMapping(value = "/api/v1/email-notifications", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Email Notifications", description = "Available Email Notification Endpoints")
public class EmailNotificationsController {

    private final EmailNotificationCommandService emailNotificationCommandService;

    public EmailNotificationsController(EmailNotificationCommandService emailNotificationCommandService) {
        this.emailNotificationCommandService = emailNotificationCommandService;
    }

    @PostMapping("/plain")
    @Operation(summary = "Send plain email", description = "Send a plain text email to one or more recipients.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Email accepted for delivery"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "502", description = "Email provider error"),
            @ApiResponse(responseCode = "503", description = "Email provider not configured")
    })
    public ResponseEntity<CommunicationAcceptedResource> sendPlainEmail(
            @Valid @RequestBody SendPlainEmailResource resource
    ) {
        var command = SendPlainEmailCommandFromResourceAssembler.toCommandFromResource(resource);
        emailNotificationCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CommunicationAcceptedResource("Plain email accepted for delivery"));
    }

    @PostMapping("/html")
    @Operation(summary = "Send HTML email", description = "Send an HTML email to one or more recipients.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Email accepted for delivery"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "502", description = "Email provider error"),
            @ApiResponse(responseCode = "503", description = "Email provider not configured")
    })
    public ResponseEntity<CommunicationAcceptedResource> sendHtmlEmail(
            @Valid @RequestBody SendHtmlEmailResource resource
    ) {
        var command = SendHtmlEmailCommandFromResourceAssembler.toCommandFromResource(resource);
        emailNotificationCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CommunicationAcceptedResource("HTML email accepted for delivery"));
    }

    @PostMapping("/templates")
    @Operation(summary = "Send template email", description = "Send a SendGrid dynamic template email to one or more recipients.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Email accepted for delivery"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "502", description = "Email provider error"),
            @ApiResponse(responseCode = "503", description = "Email provider not configured")
    })
    public ResponseEntity<CommunicationAcceptedResource> sendTemplateEmail(
            @Valid @RequestBody SendTemplateEmailResource resource
    ) {
        var command = SendTemplateEmailCommandFromResourceAssembler.toCommandFromResource(resource);
        emailNotificationCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CommunicationAcceptedResource("Template email accepted for delivery"));
    }

    @PostMapping("/rendered-templates")
    @Operation(
            summary = "Send rendered template email",
            description = "Render a built-in HTML email template with dynamic variables and send it. " +
                    "Available templates: WELCOME, SET_PASSWORD, RESET_PASSWORD."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Email accepted for delivery"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "502", description = "Email provider error"),
            @ApiResponse(responseCode = "503", description = "Email provider not configured")
    })
    public ResponseEntity<CommunicationAcceptedResource> sendRenderedTemplateEmail(
            @Valid @RequestBody SendRenderedTemplateEmailResource resource
    ) {
        var command = SendRenderedTemplateEmailCommandFromResourceAssembler.toCommandFromResource(resource);
        emailNotificationCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CommunicationAcceptedResource("Rendered template email accepted for delivery"));
    }
}
