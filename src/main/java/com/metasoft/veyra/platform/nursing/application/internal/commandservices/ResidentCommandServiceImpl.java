package com.metasoft.veyra.platform.nursing.application.internal.commandservices;

import com.metasoft.veyra.platform.nursing.application.internal.outboundservices.acl.ExternalProfileService;
import com.metasoft.veyra.platform.nursing.domain.exceptions.*;
import com.metasoft.veyra.platform.nursing.domain.model.commands.*;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Resident;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.EmergencyContact;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.LegalRepresentative;
import com.metasoft.veyra.platform.nursing.domain.services.ResidentCommandServices;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.NursingHomeRepository;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.ResidentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the ResidentCommandServices interface.
 * <p>This class is responsible for handling the commands related to the Resident aggregate.
 * It requires an ExternalProfileService, a ResidentRepository, and a NursingHomeRepository.</p>
 * @see ResidentCommandServices
 * @see ExternalProfileService
 * @see ResidentRepository
 * @see NursingHomeRepository
 */
@Service
public class ResidentCommandServiceImpl implements ResidentCommandServices {
    private final ExternalProfileService externalProfileService;
    private final ResidentRepository residentRepository;
    private final NursingHomeRepository nursingHomeRepository;

    /**
     * Constructor of the class.
     * @param externalProfileService the external profile service to be used by the class.
     * @param residentRepository the repository to be used by the class.
     * @param nursingHomeRepository the repository to be used by the class.
     */
    public ResidentCommandServiceImpl(ExternalProfileService externalProfileService,
                                      ResidentRepository residentRepository,
                                      NursingHomeRepository nursingHomeRepository) {
        this.externalProfileService = externalProfileService;
        this.residentRepository = residentRepository;
        this.nursingHomeRepository = nursingHomeRepository;
    }

    @Override
    public Long handle(CreateResidentCommand command) {
        var nursingHome = nursingHomeRepository.findById(command.nursingHomeId())
                .orElseThrow(() -> new NursingHomeNotFoundException(command.nursingHomeId()));

        var personProfileId = externalProfileService.fetchProfileByDni(command.dni());

        if (personProfileId.isEmpty()) {
            personProfileId = externalProfileService.createPersonProfile(
                    command.dni(),
                    command.firstName(),
                    command.lastName(),
                    command.birthDate(),
                    command.age(),
                    command.emailAddress(),
                    command.street(),
                    command.number(),
                    command.city(),
                    command.postalCode(),
                    command.country(),
                    command.photoData(),
                    command.photoFileName(),
                    command.phoneNumber()
            );

            if (personProfileId.isEmpty()) {
                throw new PersonProfileCreationException();
            }
        }

        if (residentRepository.existsByNursingHomeIdAndPersonProfileId(
                command.nursingHomeId(),
                personProfileId.get())) {
            throw new ResidentAlreadyExistsException(command.nursingHomeId(), personProfileId.get().personProfileId());
        }

        var emergencyContact = new EmergencyContact(
                command.emergencyContactFirstName(),
                command.emergencyContactLastName(),
                command.emergencyContactPhoneNumber()
        );

        var legalRepresentative = new LegalRepresentative(
                command.legalRepresentativeFirstName(),
                command.legalRepresentativeLastName(),
                command.legalRepresentativePhoneNumber()
        );

        var resident = new Resident(
                nursingHome,
                personProfileId.get(),
                legalRepresentative,
                emergencyContact
        );

        residentRepository.save(resident);
        return resident.getId();
    }

    @Override
    public Optional<Resident> handle(UpdateResidentCommand command) {
        var resident = residentRepository.findById(command.id())
                .orElseThrow(() -> new ResidentNotFoundException(command.id()));

        try {
            externalProfileService.updatePersonProfile(
                    resident.getPersonProfileId().personProfileId(),
                    command.dni(),
                    command.firstName(),
                    command.lastName(),
                    command.birthDate(),
                    command.Age(),
                    command.emailAddress(),
                    command.street(),
                    command.number(),
                    command.city(),
                    command.postalCode(),
                    command.country(),
                    command.photoData(),
                    command.photoFileName(),
                    command.phoneNumber()
            );

            var updatedResident = resident
                    .updateLegalRepresentative(
                            command.legalRepresentativeFirstName(),
                            command.legalRepresentativeLastName(),
                            command.legalRepresentativePhoneNumber()
                    ).updateEmergencyContact(
                            command.emergencyContactFirstName(),
                            command.emergencyContactLastName(),
                            command.emergencyContactPhoneNumber()
                    );

            var savedResident = residentRepository.save(updatedResident);
            return Optional.of(savedResident);

        } catch (Exception e) {
            throw new ResidentUpdateException(e.getMessage());
        }
    }

    @Override
    public void handle(DeleteResidentCommand command) {
        var resident = residentRepository.findById(command.residentId())
                .orElseThrow(() -> new ResidentNotFoundException(command.residentId()));

        try {
            externalProfileService.deletePersonProfile(resident.getPersonProfileId().personProfileId());
            residentRepository.delete(resident);
        } catch (Exception e) {
            throw new ResidentDeletionException(e.getMessage());
        }
    }

    @Override
    public void handle(AssignedStaffMemberToResidentCommand command) {
    }

    @Override
    public void handle(AssignRoomForResidentCommand command) {
        var nursingHome = nursingHomeRepository.findById(command.nursingHomeId())
                .orElseThrow(() -> new NursingHomeNotFoundException(command.nursingHomeId()));

        var resident = residentRepository.findById(command.residentId())
                .orElseThrow(() -> new ResidentNotFoundException(command.residentId()));

        if (!resident.getNursingHome().getId().equals(command.nursingHomeId())) {
            throw new ResidentNotBelongToNursingHomeException(command.residentId(), command.nursingHomeId());
        }

        var room = nursingHome.getRooms().getRoomByRoomNumber(command.roomNumber());
        if (room == null) {
            throw new RoomNotFoundException(command.roomNumber());
        }

        resident.assignToRoom(room);

        residentRepository.save(resident);
        nursingHomeRepository.save(nursingHome);
    }

    // inherit javadoc
    @Override
    public void handle(ChangeOfRoomForTheResidentCommand command) {
        // Validate nursing home exists
        var nursingHome = nursingHomeRepository.findById(command.nursingHomeId())
                .orElseThrow(() -> new NursingHomeNotFoundException(command.nursingHomeId()));

        // Validate resident exists
        var resident = residentRepository.findById(command.residentId())
                .orElseThrow(() -> new ResidentNotFoundException(command.residentId()));

        // Validate resident belongs to nursing home
        if (!resident.getNursingHome().getId().equals(command.nursingHomeId())) {
            throw new ResidentNotBelongToNursingHomeException(command.residentId(), command.nursingHomeId());
        }

        // Validate resident is currently assigned to a room
        if (resident.getRoom() == null) {
            throw new ResidentNotAssignedToRoomException(command.residentId());
        }

        // Validate new room exists
        var newRoom = nursingHome.getRooms().getRoomByRoomNumber(command.newRoomNumber());
        if (newRoom == null) {
            throw new RoomNotFoundException(command.newRoomNumber());
        }

        // Validate resident is not already in the new room
        if (resident.getRoom().getRoomNumber().equals(command.newRoomNumber())) {
            throw new ResidentAlreadyInRoomException(command.residentId(), command.newRoomNumber());
        }

        // Change resident's room
        resident.changeRoom(newRoom);
        residentRepository.save(resident);
        nursingHomeRepository.save(nursingHome);
    }
}