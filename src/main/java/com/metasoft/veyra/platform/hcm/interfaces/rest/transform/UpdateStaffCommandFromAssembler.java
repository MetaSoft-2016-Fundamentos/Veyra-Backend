package com.metasoft.veyra.platform.hcm.interfaces.rest.transform;

import com.metasoft.veyra.platform.hcm.domain.model.commands.UpdateStaffCommand;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.UpdateStaffResource;


public class UpdateStaffCommandFromAssembler {
    public static UpdateStaffCommand toCommandFromResource(Long id, UpdateStaffResource resource){

        try {
            return new UpdateStaffCommand(id, resource.dni(), resource.firstName()
                    , resource.lastName(), resource.birthDate(), resource.age()
                    , resource.emailAddress(), resource.street(), resource.number(), resource.city()
                    , resource.postalCode(), resource.country(), resource.photo().getBytes(), resource.photo().getOriginalFilename(), resource.phoneNumber(), resource.emergencyContactFirstName(), resource.emergencyContactLastName(), resource.phoneNumber());
        }catch (Exception e)
        {
            throw new RuntimeException("Failed to convert UpdateStaffResource to UpdateStaffCommand", e);
        }
    }

}
