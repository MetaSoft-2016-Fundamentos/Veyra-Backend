package com.metasoft.veyra.platform.communication.interfaces.rest;

import com.metasoft.veyra.platform.communication.domain.services.MessageCommandService;
import com.metasoft.veyra.platform.communication.domain.services.MessageQueryService;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.ChatMessageResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.SendMessageResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.transform.ChatMessageAssembler;
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
@RequestMapping(value = "/api/v1/conversations/{conversationId}/messages", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Conversation Messages", description = "Send and retrieve messages within a conversation")
public class ConversationMessagesController {

    private final MessageCommandService messageCommandService;
    private final MessageQueryService messageQueryService;

    public ConversationMessagesController(
            MessageCommandService messageCommandService,
            MessageQueryService messageQueryService
    ) {
        this.messageCommandService = messageCommandService;
        this.messageQueryService = messageQueryService;
    }

    @GetMapping
    @Operation(summary = "Get messages in a conversation",
            description = "Returns paginated messages ordered newest-first. The requesting user must be a participant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages retrieved"),
            @ApiResponse(responseCode = "403", description = "User is not a participant"),
            @ApiResponse(responseCode = "404", description = "Conversation not found")
    })
    public ResponseEntity<List<ChatMessageResource>> getMessages(
            @PathVariable Long conversationId,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var query = ChatMessageAssembler.toGetMessagesQuery(conversationId, userId, page, size);
        var messages = messageQueryService.handle(query);
        var resources = messages.stream().map(ChatMessageAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    @Operation(summary = "Send a message",
            description = "Sends a message in the conversation. The sender must be a participant. Triggers a push notification to all other participants.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message sent"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "403", description = "Sender is not a participant"),
            @ApiResponse(responseCode = "404", description = "Conversation or sender user not found")
    })
    public ResponseEntity<ChatMessageResource> sendMessage(
            @PathVariable Long conversationId,
            @Valid @RequestBody SendMessageResource resource
    ) {
        var command = ChatMessageAssembler.toCommand(conversationId, resource);
        var message = messageCommandService.handle(command);
        return new ResponseEntity<>(ChatMessageAssembler.toResource(message), HttpStatus.CREATED);
    }
}
