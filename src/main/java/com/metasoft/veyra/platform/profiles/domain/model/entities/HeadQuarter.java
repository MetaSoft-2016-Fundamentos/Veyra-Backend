package com.metasoft.veyra.platform.profiles.domain.model.entities;

import com.metasoft.veyra.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.metasoft.veyra.platform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class HeadQuarter extends AuditableModel {
@ManyToOne
    private BusinessProfile businessProfile;
private String name;

}
