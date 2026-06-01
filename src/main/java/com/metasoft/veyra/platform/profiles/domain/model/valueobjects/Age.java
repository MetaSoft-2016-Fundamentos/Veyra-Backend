package com.metasoft.veyra.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Age(Integer age) {
    public Age{
        if (age<=0){
            throw new IllegalArgumentException("Age cannot be 0 or less ");
        }
    }

}
