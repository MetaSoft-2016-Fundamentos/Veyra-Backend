package com.metasoft.veyra.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record  Photo(String photoUrl,
        String photoPublicId) {

    public Photo {
        if (photoUrl == null || photoUrl.isBlank()) {
            throw new IllegalArgumentException("Photo URL is required");
        }
        if (photoPublicId == null || photoPublicId.isBlank()) {
            throw new IllegalArgumentException("Photo PublicId is required");
        }
    }
}