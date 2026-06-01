package com.metasoft.veyra.platform.hcm.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record NursingHomeId(Long nursingHomeId) {
 public NursingHomeId{
     if (nursingHomeId==null||nursingHomeId<1){
         throw new IllegalArgumentException(" Nursing home id cannot be null or less 1");
     }
 }
}
