package com.metasoft.veyra.platform.communication.interfaces.rest;

import com.metasoft.veyra.platform.communication.domain.services.ConversationCommandService;
import com.metasoft.veyra.platform.communication.domain.services.ConversationQueryService;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.ConversationResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.CreateConversationResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.transform.ConversationAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/conversations", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Conversations", description = "Create conversations and manage read state")
public class ConversationsController {

    private final ConversationCommandService conversationCommandService;
    private final ConversationQueryService conversationQueryService;

    public ConversationsController(
            ConversationCommandService conversationCommandService,
            ConversationQueryService conversationQueryService
    ) {
        this.conversationCommandService = conversationCommandService;
        this.conversationQueryService = conversationQueryService;
    }

    @PostMapping
    @Operation(summary = "Create or retrieve a conversation",
            description = "Creates a new conversation. For DIRECT type, returns the existing one if it already exists (idempotent). Returns 201 when a new conversation is created, 200 when an existing one is returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New conversation created"),
            @ApiResponse(responseCode = "200", description = "Existing direct conversation returned"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "One or more participant users not found")
    })
    public ResponseEntity<ConversationResource> createConversation(
            @Valid @RequestBody CreateConversationResource resource
    ) {
        var command = ConversationAssembler.toCommand(resource);
        var result = conversationCommandService.handle(command);

        var conversation = conversationQueryService.handle(
                ConversationAssembler.toGetByIdQuery(result.conversationId(), resource.participantUserIds().get(0)));

        var conversationResource = ConversationAssembler.toResource(conversation.orElseThrow());

        if (result.isNew()) {
            return new ResponseEntity<>(conversationResource, HttpStatus.CREATED);
        }
        return ResponseEntity.ok(conversationResource);
    }

    @GetMapping("/{conversationId}")
    @Operation(summary = "Get a conversation by ID",
            description = "Returns full conversation details. The requesting user must be a participant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversation retrieved"),
            @ApiResponse(responseCode = "403", description = "User is not a participant"),
            @ApiResponse(responseCode = "404", description = "Conversation or user not found")
    })
    public ResponseEntity<ConversationResource> getConversation(
            @PathVariable Long conversationId,
            @RequestParam Long userId
    ) {
        var query = ConversationAssembler.toGetByIdQuery(conversationId, userId);
        var conversation = conversationQueryService.handle(query);
        return ResponseEntity.ok(ConversationAssembler.toResource(conversation.orElseThrow()));
    }

    @PatchMapping("/{conversationId}/read")
    @Operation(summary = "Mark a conversation as read",
            description = "Updates the lastReadAt timestamp for the requesting user in this conversation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversation marked as read"),
            @ApiResponse(responseCode = "403", description = "User is not a participant"),
            @ApiResponse(responseCode = "404", description = "Conversation or user not found")
    })
    public ResponseEntity<ConversationResource> markAsRead(
            @PathVariable Long conversationId,
            @RequestParam Long userId
    ) {
        conversationCommandService.handle(ConversationAssembler.toMarkAsReadCommand(conversationId, userId));

        var conversation = conversationQueryService.handle(
                ConversationAssembler.toGetByIdQuery(conversationId, userId));
        return ResponseEntity.ok(ConversationAssembler.toResource(conversation.orElseThrow()));
    }
}
