package com.metasoft.veyra.platform.communication.domain.model.queries;

public record GetUserPushTokensQuery(Long userId) {
    public GetUserPushTokensQuery {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }
}
