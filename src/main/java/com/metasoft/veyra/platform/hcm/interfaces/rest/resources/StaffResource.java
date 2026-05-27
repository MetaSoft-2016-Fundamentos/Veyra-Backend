package com.metasoft.veyra.platform.hcm.interfaces.rest.resources;


public record StaffResource(Long id, Long personProfileId, String status,
         String emergencyContactFirstName, String emergencyContactLastName, String emergencyContactPhoneNumber) {
}
