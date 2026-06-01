package com.metasoft.veyra.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record BusinessName(String businessName) {
    public BusinessName{
        if (businessName==null||businessName.isBlank()){
            throw new IllegalArgumentException("Business name cannot be null or blank");
        }
        if (businessName.length()<3||businessName.length()>90){
            throw new IllegalArgumentException("Business name must be between 3 and 90 characters");
        }

    }
}
