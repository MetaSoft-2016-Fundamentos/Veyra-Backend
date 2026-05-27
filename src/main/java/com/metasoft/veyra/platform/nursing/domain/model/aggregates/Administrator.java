package com.metasoft.veyra.platform.nursing.domain.model.aggregates;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.UserId;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
@Entity
@Getter
public class Administrator extends AuditableAbstractAggregateRoot<Administrator> {

    @Embedded
    private  UserId userId;
    public Administrator(){}
    public Administrator(UserId userId){
        this.userId=userId;
    }

}
