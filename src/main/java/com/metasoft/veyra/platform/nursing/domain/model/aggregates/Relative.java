package com.metasoft.veyra.platform.nursing.domain.model.aggregates;

import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.UserId;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public class Relative extends AuditableAbstractAggregateRoot<Relative> {
    @Embedded
    private UserId userId;
    public Relative(UserId userId){this.userId=userId;}
    public Relative(){}
}
