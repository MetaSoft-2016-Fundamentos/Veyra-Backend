package com.metasoft.veyra.platform.nursing.domain.model.aggregates;
import com.metasoft.veyra.platform.nursing.domain.model.commands.UpdateRelativeCommand;
import com.metasoft.veyra.platform.nursing.domain.model.events.RegisteredRelativeEvent;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.PersonProfileId;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.UserId;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailAddress;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.PersonName;
import jakarta.persistence.*;
import lombok.Getter;
@Getter
@Entity
public class Relative extends AuditableAbstractAggregateRoot<Relative> {
    @Embedded
    private UserId userId;
    @Embedded
    private EmailAddress emailAddress;
    private PersonName personName;
    @OneToOne()
    @JoinColumn( name = "resident_id")
private Resident resident;
@ManyToOne()
@JoinColumn( name = "nursing_home_id")
private NursingHome nursingHome;
    public Relative(String emailAddress, String firstName, String lastName, Resident resident,NursingHome nursingHome){
    this.emailAddress= new EmailAddress(emailAddress);
    this.userId=null;
    this.resident= resident;
    this.nursingHome=nursingHome;
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

    // dentro de la clase Relative
    public void updateRelative(UpdateRelativeCommand command, Resident newResident) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }
        if (!getId().equals(command.id())) {
            throw new IllegalArgumentException("Command ID does not match Relative ID");
        }
        if (command.emailAddress() == null || command.emailAddress().emailAddress().isBlank()) {
            throw new IllegalArgumentException("Email address cannot be null or blank");
        }
        if (command.personName() == null || command.personName().firstName().isBlank() || command.personName().lastName().isBlank()) {
            throw new IllegalArgumentException("First name and last name cannot be null or blank");
        }
        if (newResident == null) {
            throw new IllegalArgumentException("Resident cannot be null");
        }

        // Aplicar cambios al estado del agregado Relative
        this.emailAddress = command.emailAddress();
        this.personName = command.personName();
        this.resident = newResident;


    }
}
