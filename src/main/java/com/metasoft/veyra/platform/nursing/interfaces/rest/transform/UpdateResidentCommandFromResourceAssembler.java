package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.commands.UpdateResidentCommand;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.UpdateResidentResource;

import java.util.Base64;

public class UpdateResidentCommandFromResourceAssembler {
    public static UpdateResidentCommand toCommandFromResource(Long residentId,UpdateResidentResource resource){
        byte[] photoBytes = null;
        String photoFileName = null;

        if (resource.photoBase64() != null && !resource.photoBase64().isEmpty()) {
            photoBytes = decodeBase64Photo(resource.photoBase64());
            photoFileName = generatePhotoFileName(resource.dni());
        }

        return new UpdateResidentCommand(residentId,resource.dni(),resource.firstName(),
        resource.lastName(),resource.birthDate(),resource.age(),resource.emailAddress(),
        resource.street(),resource.number(),resource.city(),resource.postalCode(),resource.country(),photoBytes,photoFileName,resource.phoneNumber()
        ,resource.legalRepresentativeFirstName(),resource.legalRepresentativeLastName()
        ,resource.legalRepresentativePhoneNumber()
        ,resource.emergencyContactFirstName()
        ,resource.emergencyContactLastName() ,resource.emergencyContactPhoneNumber());

    }

    private static byte[] decodeBase64Photo(String base64Photo) {
        try {
            String base64Data = base64Photo;
            if (base64Data.contains(",")) {
                base64Data = base64Data.split(",")[1];
            }

            return Base64.getDecoder().decode(base64Data);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 photo format: " + e.getMessage());
        }
    }

    private static String generatePhotoFileName(String dni) {
        return "profile_" + dni + "_" + System.currentTimeMillis() + ".jpg";
    }
}
