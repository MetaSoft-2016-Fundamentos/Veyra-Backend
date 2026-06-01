package com.metasoft.veyra.platform.hcm.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record EmergencyContact(String firstName,String lastName,String phoneNumber) {
    public EmergencyContact{
        if (firstName==null||firstName.isBlank()){
            throw new IllegalArgumentException(" first name cannot be null o blank");
        }
        if (lastName==null||lastName.isBlank()){
            throw new IllegalArgumentException(" last name cannot be null blan");
        }
        if (phoneNumber==null||phoneNumber.isBlank()){
            throw new IllegalArgumentException("phone number cannot be null or blank");
        }
    }
}
