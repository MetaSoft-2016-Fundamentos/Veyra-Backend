package com.metasoft.veyra.platform.hcm.domain.model.queries;

public record GetStaffByUserIdQuery(Long userId) {
    public GetStaffByUserIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId cannot be null or less than or equal to 0");
        }
    }
}