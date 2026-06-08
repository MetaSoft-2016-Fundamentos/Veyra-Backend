package com.metasoft.veyra.platform.profiles.interfaces.rest.transform;

import com.metasoft.veyra.platform.profiles.domain.model.commands.CreatePersonProfileCommand;
import com.metasoft.veyra.platform.profiles.interfaces.rest.resources.CreatePersonProfileResource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public class CreatePersonProfileCommandFromResourceAssembler {

    public static CreatePersonProfileCommand toCommandFromResource(
            CreatePersonProfileResource resource){
        try {
            return new CreatePersonProfileCommand(
                    resource.dni(),
                    resource.firstName(),
                    resource.lastName(),
                    resource.birthDate(),
                    resource.age(),
                    resource.emailAddress(),
                    resource.street(),
                    resource.number(),
                    resource.city(),
                    resource.postalCode(),
                    resource.country(),
                  resource.photo().getBytes(),
                    resource.photo().getOriginalFilename(),
                    resource.phoneNumber()
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading photo: " + e.getMessage());
        }
    }
}