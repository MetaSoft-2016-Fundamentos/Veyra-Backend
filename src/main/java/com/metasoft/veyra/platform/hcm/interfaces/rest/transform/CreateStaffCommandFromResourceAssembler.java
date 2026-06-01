package com.metasoft.veyra.platform.hcm.interfaces.rest.transform;

import com.metasoft.veyra.platform.hcm.domain.model.commands.CreateStaffCommand;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.CreateStaffResource;

import java.util.Base64;

public class CreateStaffCommandFromResourceAssembler {
    public static CreateStaffCommand toCommandFromResource(CreateStaffResource resource,Long nursingHomeId)
    {
        try {
            return new CreateStaffCommand(nursingHomeId, resource.dni(), resource.firstName(), resource.lastName(), resource.birthDate(), resource.age(), resource.emailAddress()
                    , resource.street(), resource.number(), resource.city(), resource.postalCode(), resource.country(), resource.photo().getBytes(), resource.photo().getOriginalFilename(), resource.phoneNumber(),
                    resource.emergencyContactFirstName(), resource.emergencyContactLastName(), resource.emergencyContactPhoneNumber());
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert CreateStaffResource to CreateStaffCommand", e);
        }
    }
}
