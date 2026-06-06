package com.metasoft.veyra.platform.nursing.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record LegalRepresentative(String firstName,String lastName,String phoneNumber) {
    public LegalRepresentative{
        if (firstName == null|| lastName==null|| phoneNumber==null) {
            throw new IllegalArgumentException("firstName or lastName or phoneNumber cannot be null");
        }
    }

}