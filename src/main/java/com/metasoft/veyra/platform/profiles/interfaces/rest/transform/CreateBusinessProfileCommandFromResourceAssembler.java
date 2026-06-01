package com.metasoft.veyra.platform.profiles.interfaces.rest.transform;

import com.metasoft.veyra.platform.profiles.domain.model.commands.CreateBusinessProfileCommand;
import com.metasoft.veyra.platform.profiles.interfaces.rest.resources.CreateBusinessProfileResource;

import java.io.IOException;
import java.util.Base64;

public class CreateBusinessProfileCommandFromResourceAssembler {

    public static CreateBusinessProfileCommand toCommandFromResource(
            CreateBusinessProfileResource resource) {
        try {
            return new CreateBusinessProfileCommand(
                    resource.businessName(),
                    resource.emailAddress(),
                    resource.phoneNumber(),
                    resource.street(),
                    resource.number(),
                    resource.city(),
                    resource.postalCode(),
                    resource.country(),
                    resource.photo().getBytes(),
                    resource.photo().getOriginalFilename(),
                    resource.ruc()
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading photo: " + e.getMessage());
        }
    }
}
