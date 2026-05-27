package com.metasoft.veyra.platform.nursing.domain.model.valueobjects;

public record BusinessProfileId(Long businessProfileId) {
    public BusinessProfileId {
        if (businessProfileId == null) {
            throw new IllegalArgumentException("businessProfileId cannot be null");
        }
        if (businessProfileId < 1) {
            throw new IllegalArgumentException("businessProfileId cannot be less than 1");
        }
    }
}
