package com.metasoft.veyra.platform.profiles.interfaces.rest.transform;

import com.metasoft.veyra.platform.profiles.domain.model.commands.CreateBusinessProfileCommand;
import com.metasoft.veyra.platform.profiles.interfaces.rest.resources.CreateBusinessProfileResource;

import java.util.Base64;

public class CreateBusinessProfileCommandFromResourceAssembler {
    public static CreateBusinessProfileCommand toCommandFromResource(CreateBusinessProfileResource resource){
        byte[] photoBytes = decodeBase64Photo(resource.photoBase64());
        String photoFileName = generatePhotoFileName(resource.ruc());
        return new CreateBusinessProfileCommand(
                resource.businessName(),
                resource.emailAddress(),
                resource.phoneNumber(),
                resource.street(),
                resource.number(),
                resource.city(),
                resource.postalCode(),
                resource.country(),
                photoBytes,
                photoFileName,
                resource.ruc()        );
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
