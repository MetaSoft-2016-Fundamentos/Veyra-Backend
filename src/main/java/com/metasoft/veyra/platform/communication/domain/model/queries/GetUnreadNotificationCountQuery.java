package com.metasoft.veyra.platform.communication.domain.model.queries;

public record GetUnreadNotificationCountQuery(Long userId) {
    public GetUnreadNotificationCountQuery {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }
}
