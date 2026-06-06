package com.metasoft.veyra.platform.nursing.domain.model.valueobjects;

public record EmergencyContact(String firstName,String lastName,String phoneNumber) {
    public EmergencyContact{
        if (firstName == null || lastName == null || phoneNumber == null) {
            throw new IllegalArgumentException("firstName or lastName or phoneNumber cannot be null");
        }
    }
}
