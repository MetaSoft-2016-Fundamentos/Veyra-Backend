package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateNursingHomeCommand;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateNursingHomeResource;

import java.io.IOException;


public class CreateNursingHomeCommandFromResourceAssembler {

    public static CreateNursingHomeCommand toCommandFromResource(CreateNursingHomeResource resource,Long adminId) {
        try {
            return new CreateNursingHomeCommand(adminId, resource.businessName(), resource.emailAddress(), resource.phoneNumber(), resource.street()
                    , resource.number(), resource.city(), resource.postalCode(), resource.country(), resource.photo().getBytes(), resource.photo().getOriginalFilename(), resource.ruc());
        }catch (IOException e){
            throw new RuntimeException("Error processing the photo", e);
        }
    }



}
