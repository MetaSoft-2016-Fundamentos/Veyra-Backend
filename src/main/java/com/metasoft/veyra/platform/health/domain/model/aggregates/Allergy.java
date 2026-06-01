package com.metasoft.veyra.platform.health.domain.model.aggregates;

import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.SeverityLevel;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.TypeOfAllergy;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Allergy  extends AuditableAbstractAggregateRoot<Allergy> {
    @Embedded
private ResidentId residentId;
    @Column(nullable = false)
private String reaction ;
    @Column(nullable = false)
    private String allergenName;
 @Enumerated(EnumType.STRING)
 private TypeOfAllergy typeOfAllergy;
    @Enumerated(EnumType.STRING)
 private SeverityLevel severityLevel;
    public Allergy(){}
    public Allergy(ResidentId residentId,String reaction,String allergenName,TypeOfAllergy typeOfAllergy,SeverityLevel severityLevel)
    {
        this.residentId=residentId;
        this.reaction=reaction;
        this.allergenName=allergenName;
        this.typeOfAllergy=typeOfAllergy;
        this.severityLevel=severityLevel;
    }

}
