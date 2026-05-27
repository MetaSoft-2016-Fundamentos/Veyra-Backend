package com.metasoft.veyra.platform.payments.domain.model.valueobjects;

public record UserId(Long userId) {
    public UserId{
        if (userId==null||userId<1){
            throw new IllegalArgumentException("cannot be null or then less 1");
        }
    }
}
