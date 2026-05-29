package com.metasoft.veyra.platform.nursing.domain.model.aggregates;
import com.metasoft.veyra.platform.nursing.domain.model.events.RegisteredRelativeEvent;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.UserId;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailAddress;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.PersonName;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
@Getter
@Entity
public class Relative extends AuditableAbstractAggregateRoot<Relative> {
    @Embedded
    private UserId userId;
    @Embedded
    private EmailAddress emailAddress;
    private PersonName personName;
    public Relative(String emailAddress, String firstName, String lastName){
    this.emailAddress= new EmailAddress(emailAddress);
    this.userId=null;
    this.personName= new PersonName(firstName, lastName);
        this.addDomainEvent(new RegisteredRelativeEvent(this,this.emailAddress.emailAddress(),this.personName.firstName(),this.personName.lastName()));
    }

    public Relative(){

    }
    public void linkToUser(Long  userId){
        if (userId == null || userId < 1) {
            throw new IllegalArgumentException("userId must be a positive number.");
        }
        if (this.userId!=null){
            throw new IllegalArgumentException("user is already linked to this relative");
        }
        this.userId=new UserId(userId);
    }
}
