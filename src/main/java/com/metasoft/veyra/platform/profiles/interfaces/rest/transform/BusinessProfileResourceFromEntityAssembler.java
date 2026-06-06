package com.metasoft.veyra.platform.profiles.interfaces.rest.transform;

import com.metasoft.veyra.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.metasoft.veyra.platform.profiles.interfaces.rest.resources.BusinessProfileResource;

public class BusinessProfileResourceFromEntityAssembler {
    public static BusinessProfileResource  toResourceFromEntity(BusinessProfile entity){
        return new BusinessProfileResource(entity.getId(),
                entity.getBusinessName().businessName(),
                entity.getEmailAddress().emailAddress(),
                entity.getPhoneNumber().phoneNumber(),
                entity.getStreetAddress().getStreetAddress(),
                entity.getPhoto().photoUrl(),
                entity.getRuc().ruc());
    }
}
