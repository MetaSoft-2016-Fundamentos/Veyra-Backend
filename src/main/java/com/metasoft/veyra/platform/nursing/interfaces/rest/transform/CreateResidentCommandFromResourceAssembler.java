package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateResidentCommand;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateResidentResource;

import java.io.IOException;

public class CreateResidentCommandFromResourceAssembler {



    public static CreateResidentCommand toCommandFromResource(CreateResidentResource resource, Long nursingHomeId)
    {
      try {
          return new CreateResidentCommand(nursingHomeId,resource.dni(),resource.firstName(),resource.lastName(),resource.birthDate(),
                  resource.age(),resource.emailAddress(),resource.street(),resource.number(),resource.city(),resource.postalCode(),
                  resource.country(),resource.photo().getBytes(),resource.photo().getOriginalFilename(),resource.phoneNumber()
                  ,resource.legalRepresentativeFirstName(),resource.legalRepresentativeLastName(),resource.legalRepresentativePhoneNumber(),
                  resource.emergencyContactFirstName(),resource.emergencyContactLastName(),resource.emergencyContactPhoneNumber()
          );
      }catch (IOException e){throw new IllegalArgumentException("Error processing the photo file: " + e.getMessage(), e);}

    }

}
