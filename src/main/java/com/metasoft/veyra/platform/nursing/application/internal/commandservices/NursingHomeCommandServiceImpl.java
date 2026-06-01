package com.metasoft.veyra.platform.nursing.application.internal.commandservices;

import com.metasoft.veyra.platform.nursing.application.internal.outboundservices.acl.ExternalProfileService;
import com.metasoft.veyra.platform.nursing.domain.exceptions.AdministratorNotFoundException;
import com.metasoft.veyra.platform.nursing.domain.exceptions.BusinessProfileCreationException;
import com.metasoft.veyra.platform.nursing.domain.exceptions.NursingHomeAlreadyExistsException;
import com.metasoft.veyra.platform.nursing.domain.exceptions.NursingHomeNotFoundException;
import com.metasoft.veyra.platform.nursing.domain.exceptions.NursingHomeWithBusinessProfileAlreadyExistsException;
import com.metasoft.veyra.platform.nursing.domain.exceptions.RoomCreationException;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.NursingHome;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateARoomToTheNursingHomeCommand;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateNursingHomeCommand;
import com.metasoft.veyra.platform.nursing.domain.services.NursingHomeCommandServices;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.AdministratorRepository;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.NursingHomeRepository;
import org.springframework.stereotype.Service;

/**
 * Implementation of the NursingHomeCommandServices interface.
 * <p>This class is responsible for handling the commands related to the NursingHome aggregate.
 * It requires a NursingHomeRepository, an ExternalProfileService, and an AdministratorRepository.</p>
 * @see NursingHomeCommandServices
 * @see NursingHomeRepository
 * @see ExternalProfileService
 * @see AdministratorRepository
 */
@Service
public class NursingHomeCommandServiceImpl implements NursingHomeCommandServices {
    private final NursingHomeRepository nursingHomeRepository;
    private final ExternalProfileService externalProfileService;
    private final AdministratorRepository administratorRepository;

    /**
     * Constructor of the class.
     * @param nursingHomeRepository the repository to be used by the class.
     * @param externalProfileService the external profile service to be used by the class.
     * @param administratorRepository the repository to be used by the class.
     */
    public NursingHomeCommandServiceImpl(NursingHomeRepository nursingHomeRepository,
                                         ExternalProfileService externalProfileService,
                                         AdministratorRepository administratorRepository) {
        this.nursingHomeRepository = nursingHomeRepository;
        this.externalProfileService = externalProfileService;
        this.administratorRepository = administratorRepository;
    }

    // inherit javadoc
    @Override
    public Long handle(CreateNursingHomeCommand command) {
        // Validate administrator exists
        var administratorId = administratorRepository.findById(command.administratorId())
                .orElseThrow(() -> new AdministratorNotFoundException(command.administratorId()));

        // Validate nursing home doesn't already exist for this administrator
        var existingNursingHome = nursingHomeRepository.findByAdministratorId(administratorId.getId());
        if (existingNursingHome.isPresent()) {
            throw new NursingHomeAlreadyExistsException(command.administratorId());
        }

        // Fetch or create business profile
        var businessProfileIde = externalProfileService.fetchBusinessProfileByRuc(command.ruc());
        if (businessProfileIde.isEmpty()) {
            businessProfileIde = externalProfileService.createBusinessProfile(
                    command.businessName(),
                    command.emailAddress(),
                    command.phoneNumber(),
                    command.street(),
                    command.number(),
                    command.city(),
                    command.postalCode(),
                    command.country(),
                    command.photoData(),
                    command.photoFileName(),
                    command.ruc());
        } else {
            // Validate business profile is not already associated with another nursing home
                var businessProfileId = businessProfileIde.get();
            nursingHomeRepository.findByBusinessProfileId(businessProfileId).ifPresent(nursingHome -> {
                throw new NursingHomeWithBusinessProfileAlreadyExistsException(
                        nursingHome.getId(),
                        businessProfileId.businessProfileId());
            });
        }

        // Validate business profile was created successfully
        if (businessProfileIde.isEmpty()) {
            throw new BusinessProfileCreationException();
        }

        // Create and save nursing home
        var nursingHome = new NursingHome(businessProfileIde.get(), administratorId);
        nursingHomeRepository.save(nursingHome);
        return nursingHome.getId();
    }

    // inherit javadoc
    @Override
    public void handle(CreateARoomToTheNursingHomeCommand command) {
        // Validate nursing home exists
        var nursingHome = nursingHomeRepository.findById(command.nursingHomeId())
                .orElseThrow(() -> new NursingHomeNotFoundException(command.nursingHomeId()));

        try {
            nursingHome.addRoom(command.capacity(), command.type(), command.roomNumber());
            nursingHomeRepository.save(nursingHome);
        } catch (IllegalArgumentException e) {
            throw new RoomCreationException(e.getMessage());
        }
    }
}