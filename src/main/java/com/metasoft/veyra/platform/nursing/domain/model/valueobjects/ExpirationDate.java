package com.metasoft.veyra.platform.nursing.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public record ExpirationDate(LocalDate expirationDate) {
    public ExpirationDate{
        if (expirationDate==null){
            throw new IllegalArgumentException(" expiration date cannot be null");
        }
        if (expirationDate.isBefore(LocalDate.now())){
            throw new IllegalArgumentException(" expiration date cannot be before now");
        }
    }
}
