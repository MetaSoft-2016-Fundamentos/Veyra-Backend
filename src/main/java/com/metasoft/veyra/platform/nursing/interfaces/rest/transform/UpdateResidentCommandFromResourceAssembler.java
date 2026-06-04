package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.commands.UpdateResidentCommand;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.UpdateResidentResource;

import java.io.IOException;

public class UpdateResidentCommandFromResourceAssembler {
    public static UpdateResidentCommand toCommandFromResource(Long residentId,UpdateResidentResource resource){
        byte[] photoBytes = null;
        String photoFileName = null;

        if (resource.photo() != null) {
            try {
                photoBytes = resource.photo().getBytes();
                photoFileName = resource.photo().getOriginalFilename();
            } catch (IOException e) {
                throw new IllegalArgumentException("Error processing the photo file: " + e.getMessage(), e);
            }
        }

        return new UpdateResidentCommand(residentId,resource.dni(),resource.firstName(),
        resource.lastName(),resource.birthDate(),resource.age(),resource.emailAddress(),
        resource.street(),resource.number(),resource.city(),resource.postalCode(),resource.country(),photoBytes,photoFileName,resource.phoneNumber()
        ,resource.legalRepresentativeFirstName(),resource.legalRepresentativeLastName()
        ,resource.legalRepresentativePhoneNumber()
        ,resource.emergencyContactFirstName()
        ,resource.emergencyContactLastName() ,resource.emergencyContactPhoneNumber());

    }
}
