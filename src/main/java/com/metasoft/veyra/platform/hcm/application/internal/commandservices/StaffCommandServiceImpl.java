package com.metasoft.veyra.platform.hcm.application.internal.commandservices;

import com.metasoft.veyra.platform.hcm.application.internal.outboundservices.acl.ExternalNursingService;
import com.metasoft.veyra.platform.hcm.application.internal.outboundservices.acl.ExternalProfileService;
import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.hcm.domain.model.commands.*;
import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.EmergencyContact;
import com.metasoft.veyra.platform.hcm.domain.services.StaffCommandServices;
import com.metasoft.veyra.platform.hcm.infrastructure.persistence.jpa.repositories.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class StaffCommandServiceImpl implements StaffCommandServices {
    private final StaffRepository staffRepository;
    private  final ExternalNursingService externalNursingService;
    private final ExternalProfileService externalProfileService;
    public StaffCommandServiceImpl(StaffRepository staffRepository, ExternalNursingService externalNursingService, ExternalProfileService externalProfileService) {
        this.staffRepository = staffRepository;
        this.externalNursingService = externalNursingService;
        this.externalProfileService = externalProfileService;
    }

    @Override
    public  Long handle(CreateStaffCommand command) {
        var personProfileId = externalProfileService.fetchProfileByDni(command.dni());
        var nursingHomeId = externalNursingService.fetchNursingHomeById(command.nursingHomeId());

        if (nursingHomeId.isEmpty()){
            throw new IllegalArgumentException("Nursing home id does not exist");
        }

        if (personProfileId.isEmpty()){
            personProfileId = externalProfileService.createPersonProfile(
                    command.dni(), command.firstName(), command.lastName(),
                    command.birthDate(), command.Age(), command.emailAddress(),
                    command.street(), command.number(), command.city(),
                    command.postalCode(), command.country(), command.photoData(), command.photoFileName(),
                    command.phoneNumber()
            );

            if (personProfileId.isEmpty()){
                throw new IllegalArgumentException("Unable to create person profile");
            }
        }

        if (externalNursingService.existsResidentByPersonProfile(personProfileId.get().id())){
            throw new IllegalArgumentException("Person with DNI " + command.dni() + " is already registered as a resident");
        }

        staffRepository.findByPersonProfileIdAndNursingHomeId(
                personProfileId.get(),
                nursingHomeId.get()
        ).ifPresent(staff -> {
            throw new IllegalArgumentException(String.format(
                    "Person with DNI %s is already registered as staff in this nursing home (Staff ID: %s)",
                    command.dni(),
                    staff.getPersonProfileId().id()
            ));
        });

        var emergencyContact = new EmergencyContact(
                command.emergencyContactFirstName(),
                command.emergencyContactLastName(),
                command.emergencyContactPhoneNumber()
        );

        var staff = new Staff(personProfileId.get(), nursingHomeId.get(), emergencyContact);
        staffRepository.save(staff);
        return staff.getId();
    }

    @Override
    public Optional<Staff> handle(UpdateStaffCommand command) {
        var staffId=staffRepository.findById(command.id());
        if (staffId.isEmpty()){
            throw new IllegalArgumentException("Employee with id " + command.id() + " does not exist.");
        }
        var staffUpdate=staffId.get();
        try {
            externalProfileService.updatePersonProfile(staffId.get().getPersonProfileId().id(), command.dni(), command.firstName(), command.lastName(),
                    command.birthDate(), command.Age(), command.emailAddress(), command.street(),
                    command.number(), command.city(), command.postalCode(), command.country(),
                    command.photoData(),command.photoFileName(), command.phoneNumber());
            var updateStaff= staffUpdate.updateEmergencyContact(command.emergencyContactFirstName(),command.emergencyContactLastName(),command.emergencyContactPhoneNumber());
            var savedStaff= staffRepository.save(updateStaff);
            return Optional.of(savedStaff);
        }catch (Exception e){
            throw new IllegalArgumentException("Error while updating staff: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(DeleteStaffCommand command) {

    }

    @Override
    public void handle(AddContractToStaffMemberCommand command) {
        if (!staffRepository.existsById(command.staffMemberId())){
            throw new IllegalArgumentException("Staff with id %s not found ".formatted(command.staffMemberId()));
        }
        else {
            try {
                Staff staff = staffRepository.findById(command.staffMemberId())
                        .orElseThrow(() -> new IllegalArgumentException("Staff with id %s not found".formatted(command.staffMemberId())));
                staff.addContractToHistory(command.startDate(),command.endDate(),command.typeOfContract()
                        ,command.staffRole(),command.workShift());
                staffRepository.save(staff);
            } catch (Exception e){
                throw new IllegalArgumentException("Error while adding contract to staff: %s".formatted(e.getMessage()));
            }
        }
    }

    @Override
    public void handle(UpdateContractStatusCommand command) {
        try {
            Staff staff = staffRepository.findById(command.staffMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("Staff with id %s not found".formatted(command.staffMemberId())));
            staff.updateContractStatus(command.contractId(), command.newStatus());
            staffRepository.save(staff);
        } catch (IllegalArgumentException e){
            throw e;
        } catch (Exception e){
            throw new IllegalArgumentException("Error while updating contract status: %s".formatted(e.getMessage()));
        }
    }
}
