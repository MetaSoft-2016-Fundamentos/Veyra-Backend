package com.metasoft.veyra.platform.hcm.interfaces.rest.transform;


import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.StaffResource;

public class StaffResourceFromEntityAssembler {
   public static StaffResource toResourceFromEntity(Staff entity){
       return new StaffResource(entity.getId(),entity.getPersonProfileId().id(),entity.getStaffStatus().name(),entity.getEmergencyContact().firstName(),entity.getEmergencyContact().lastName(),entity.getEmergencyContact().phoneNumber());
   }
}
