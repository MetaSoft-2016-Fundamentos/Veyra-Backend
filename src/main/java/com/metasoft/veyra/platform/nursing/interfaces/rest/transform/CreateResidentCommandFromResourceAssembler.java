package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateResidentCommand;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateResidentResource;

import java.util.Base64;

public class CreateResidentCommandFromResourceAssembler {



    public static CreateResidentCommand toCommandFromResource(CreateResidentResource resource, Long nursingHomeId)
    {
        byte[] photoBytes = decodeBase64Photo(resource.photoBase64());
        String photoFileName = generatePhotoFileName(resource.dni());
      return new CreateResidentCommand(nursingHomeId,resource.dni(),resource.firstName(),resource.lastName(),resource.birthDate(),
       resource.age(),resource.emailAddress(),resource.street(),resource.number(),resource.city(),resource.postalCode(),
              resource.country(),photoBytes,photoFileName,resource.phoneNumber()
              ,resource.legalRepresentativeFirstName(),resource.legalRepresentativeLastName(),resource.legalRepresentativePhoneNumber(),
              resource.emergencyContactFirstName(),resource.emergencyContactLastName(),resource.emergencyContactPhoneNumber()
      ) ;
    }
    private static byte[] decodeBase64Photo(String base64Photo) {
        if (base64Photo == null || base64Photo.isEmpty()) {
            throw new IllegalArgumentException("Photo is required");
        }

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
