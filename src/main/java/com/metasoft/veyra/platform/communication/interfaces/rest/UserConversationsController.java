package com.metasoft.veyra.platform.communication.interfaces.rest;

import com.metasoft.veyra.platform.communication.domain.services.ConversationCommandService;
import com.metasoft.veyra.platform.communication.domain.services.ConversationQueryService;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.ConversationSummaryResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.UnreadConversationCountResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.transform.ConversationAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/users/{userId}/conversations", produces = APPLICATION_JSON_VALUE)
@Tag(name = "User Conversations", description = "Retrieve conversations and unread counts scoped to a user")
public class UserConversationsController {

    private final ConversationQueryService conversationQueryService;
    private final ConversationCommandService conversationCommandService;

    public UserConversationsController(
            ConversationQueryService conversationQueryService,
            ConversationCommandService conversationCommandService
    ) {
        this.conversationQueryService = conversationQueryService;
        this.conversationCommandService = conversationCommandService;
    }

    @GetMapping
    @Operation(summary = "List conversations for a user",
            description = "Returns all conversations where the user is a participant, ordered by most recent message.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversations retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<ConversationSummaryResource>> getConversations(@PathVariable Long userId) {
        var query = ConversationAssembler.toGetByUserQuery(userId);
        var conversations = conversationQueryService.handle(query);
        var resources = conversations.stream().map(ConversationAssembler::toSummaryResource).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get unread conversation count",
            description = "Returns the number of conversations with unread messages for badge display.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UnreadConversationCountResource> getUnreadCount(@PathVariable Long userId) {
        var count = conversationQueryService.handle(ConversationAssembler.toUnreadCountQuery(userId));
        return ResponseEntity.ok(new UnreadConversationCountResource(count));
    }
}
