package com.metasoft.veyra.platform.nursing.domain.model.commands;

public record AssignUserToRelativeCommand(String email,  Long userId) {
    public AssignUserToRelativeCommand{
        if (email==null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (userId==null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }
}
