package com.metasoft.veyra.platform.communication.interfaces.rest;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.NotificationStatus;
import com.metasoft.veyra.platform.communication.domain.services.UserNotificationCommandService;
import com.metasoft.veyra.platform.communication.domain.services.UserNotificationQueryService;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.MarkAllNotificationsReadResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.UnreadNotificationCountResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.UserNotificationResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.transform.UserNotificationAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/users/{userId}/notifications", produces = APPLICATION_JSON_VALUE)
@Tag(name = "User Notifications", description = "In-app notification inbox with read/unread status")
public class UserNotificationsController {

    private final UserNotificationCommandService userNotificationCommandService;
    private final UserNotificationQueryService userNotificationQueryService;

    public UserNotificationsController(
            UserNotificationCommandService userNotificationCommandService,
            UserNotificationQueryService userNotificationQueryService
    ) {
        this.userNotificationCommandService = userNotificationCommandService;
        this.userNotificationQueryService = userNotificationQueryService;
    }

    @GetMapping
    @Operation(summary = "List user notifications", description = "Returns notifications ordered by newest first. Optional filter by status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<UserNotificationResource>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(required = false) NotificationStatus status
    ) {
        var query = UserNotificationAssembler.toQuery(userId, status);
        var notifications = userNotificationQueryService.handle(query);
        var resources = notifications.stream().map(UserNotificationAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get unread notification count", description = "Returns the number of unread notifications for badge display.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UnreadNotificationCountResource> getUnreadCount(@PathVariable Long userId) {
        var count = userNotificationQueryService.handle(UserNotificationAssembler.toUnreadCountQuery(userId));
        return ResponseEntity.ok(new UnreadNotificationCountResource(count));
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "Mark notification as read", description = "Marks a single notification as read.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification marked as read"),
            @ApiResponse(responseCode = "404", description = "User or notification not found")
    })
    public ResponseEntity<UserNotificationResource> markAsRead(
            @PathVariable Long userId,
            @PathVariable Long notificationId
    ) {
        var command = UserNotificationAssembler.toMarkAsReadCommand(userId, notificationId);
        var notification = userNotificationCommandService.handle(command);
        return ResponseEntity.ok(UserNotificationAssembler.toResource(notification));
    }

    @PatchMapping("/read-all")
    @Operation(summary = "Mark all notifications as read", description = "Marks every unread notification for the user as read.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications marked as read"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<MarkAllNotificationsReadResource> markAllAsRead(@PathVariable Long userId) {
        var command = UserNotificationAssembler.toMarkAllAsReadCommand(userId);
        int markedCount = userNotificationCommandService.handle(command);
        return ResponseEntity.ok(new MarkAllNotificationsReadResource(markedCount));
    }
}
