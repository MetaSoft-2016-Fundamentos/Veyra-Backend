package com.metasoft.veyra.platform.nursing.application.internal.commandservices;
import com.metasoft.veyra.platform.nursing.domain.exceptions.NursingHomeNotFoundException;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Relative;
import com.metasoft.veyra.platform.nursing.domain.model.commands.AssignUserToRelativeCommand;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateRelativeCommand;
import com.metasoft.veyra.platform.nursing.domain.model.commands.UpdateRelativeCommand;
import com.metasoft.veyra.platform.nursing.domain.model.events.RegisteredRelativeEvent; // Import added
import com.metasoft.veyra.platform.nursing.domain.services.RelativeCommandService;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.NursingHomeRepository;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.RelativeRepository;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.ResidentRepository;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailAddress;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RelativeCommandServiceImpl implements RelativeCommandService {
    private final RelativeRepository relativeRepository;
    private final NursingHomeRepository nursingHomeRepository;
    private final ResidentRepository residentRepository;
    public RelativeCommandServiceImpl(RelativeRepository relativeRepository, ResidentRepository residentRepository, NursingHomeRepository nursingHomeRepository) {
        this.relativeRepository = relativeRepository;
        this.nursingHomeRepository = nursingHomeRepository;
        this.residentRepository = residentRepository;
    }

    @Override
    public Long handle(CreateRelativeCommand command) {
        if (relativeRepository.existsByEmailAddress(new EmailAddress(command.email()))){
            throw  new IllegalArgumentException("Relative with email " + command.email() + " already exists.");
        }
        var residentId = residentRepository.findById(command.residentId()).orElseThrow(() -> new IllegalArgumentException("Resident with id " + command.residentId() + " not found."));
        var nursingHomeId = nursingHomeRepository.findById(command.nursingHomeId()).orElseThrow(() ->new NursingHomeNotFoundException(command.nursingHomeId()));
        var relative = new Relative(command.email(),command.firstname(),command.lastname(),residentId,nursingHomeId);
        try {
            relativeRepository.save(relative);
            // Add the domain event AFTER saving the aggregate
            relative.addDomainEvent(new RegisteredRelativeEvent(relative, relative.getEmailAddress().emailAddress(), relative.getPersonName().firstName(), relative.getPersonName().lastName()));
            return relative.getId();
        }catch (Exception e){
            throw new RuntimeException("Error creating relative: " + e.getMessage(), e);
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
    public Optional<Relative> handle(UpdateRelativeCommand command) {
        var relative = relativeRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Relative with id " + command.id() + " not found."));

        var newResident = residentRepository.findById(command.residentId())
                .orElseThrow(() -> new IllegalArgumentException("Resident with id " + command.residentId() + " not found."));

        relative.updateRelative(command, newResident);

        try {
            relativeRepository.save(relative);
            return Optional.of(relative);
        } catch (Exception e) {
            throw new RuntimeException("Error updating relative: " + e.getMessage(), e);
        }
    }
}