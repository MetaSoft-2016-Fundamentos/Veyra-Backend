package com.metasoft.veyra.platform.nursing.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record StaffMemberId(Long staffMemberId) {
    public StaffMemberId{
        if (staffMemberId==null|| staffMemberId<=0){
          throw new IllegalArgumentException(" staff member id cannot be null o less 0")  ;
        }
    }
}
