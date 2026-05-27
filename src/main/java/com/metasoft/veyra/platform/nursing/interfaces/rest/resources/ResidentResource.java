package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;


public record ResidentResource(Long id, Long personProfileId,String status,String legalRepresentativeFirstName
        , String legalRepresentativeLastName, String legalRepresentativePhoneNumber
        , String emergencyContactFirstName, String emergencyContactLastName, String emergencyContactPhoneNumber,Long roomId) {
}
