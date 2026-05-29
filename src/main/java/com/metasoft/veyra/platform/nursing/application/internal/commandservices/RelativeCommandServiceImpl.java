package com.metasoft.veyra.platform.nursing.application.internal.commandservices;
import com.metasoft.veyra.platform.nursing.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Relative;
import com.metasoft.veyra.platform.nursing.domain.model.commands.AssignResidentForRelativeCommand;
import com.metasoft.veyra.platform.nursing.domain.model.commands.AssignUserToRelativeCommand;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateRelativeCommand;
import com.metasoft.veyra.platform.nursing.domain.services.RelativeCommandService;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.RelativeRepository;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.ResidentRepository;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailAddress;
import org.springframework.stereotype.Service;
@Service
public class RelativeCommandServiceImpl implements RelativeCommandService {
    private final RelativeRepository relativeRepository;
 private  final ExternalIamService externalIamService;
 private final ResidentRepository residentRepository;
    public RelativeCommandServiceImpl(RelativeRepository relativeRepository, ExternalIamService externalIamService, ResidentRepository residentRepository) {
        this.relativeRepository = relativeRepository;
        this.externalIamService = externalIamService;
        this.residentRepository = residentRepository;
    }
    @Override
    public Long handle(CreateRelativeCommand command) {
    if (relativeRepository.findByEmailAddress(new EmailAddress(command.email())).isPresent()) {
        throw new IllegalArgumentException("Relative with email " + command.email() + " already exists.");
    }
    try {
        var relative = new Relative(command.email(),command.firstname(),command.lastname());
        relativeRepository.save(relative);
        return relative.getId();
    }catch (Exception e){
        throw new RuntimeException("Failed to create relative: " + e.getMessage(), e);
    }
    }
    @Override
    public Long handle(AssignUserToRelativeCommand command) {
       var relative = relativeRepository.findByEmailAddress(new EmailAddress(command.email())).orElseThrow(() -> new IllegalArgumentException("Relative with email " + command.email() + " not found."));
           relative.linkToUser(command.userId());
           relativeRepository.save(relative);
           return relative.getId();
    }

    @Override
    public void handle(AssignResidentForRelativeCommand command) {
        var relative = relativeRepository.findById(command.relativeId()).orElseThrow(() -> new IllegalArgumentException("Relative with id " + command.relativeId() + " not found."));
        var resident = residentRepository.findById(command.residentId()).orElseThrow(() -> new IllegalArgumentException("Resident with id " + command.residentId() + " not found."));
        resident.assignedRelativeToResidentCommand(relative);

    }

}