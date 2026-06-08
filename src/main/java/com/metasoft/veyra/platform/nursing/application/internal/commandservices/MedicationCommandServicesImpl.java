package com.metasoft.veyra.platform.nursing.application.internal.commandservices;

import com.metasoft.veyra.platform.nursing.domain.exceptions.MedicationAlreadyExistsException;
import com.metasoft.veyra.platform.nursing.domain.exceptions.MedicationCreationException;
import com.metasoft.veyra.platform.nursing.domain.exceptions.ResidentNotActiveException;
import com.metasoft.veyra.platform.nursing.domain.exceptions.ResidentNotFoundException;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Medication;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateMedicationCommand;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.DrugPresentation;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.ExpirationDate;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.ResidentState;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.Stock;
import com.metasoft.veyra.platform.nursing.domain.services.MedicationCommandServices;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.MedicationRepository;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.ResidentRepository;
import org.springframework.stereotype.Service;

/**
 * Implementation of the MedicationCommandServices interface.
 * <p>This class is responsible for handling the commands related to the Medication aggregate.
 * It requires a MedicationRepository and a ResidentRepository.</p>
 * @see MedicationCommandServices
 * @see MedicationRepository
 * @see ResidentRepository
 */
@Service
public class MedicationCommandServicesImpl implements MedicationCommandServices {
    private final MedicationRepository medicationRepository;
    private final ResidentRepository residentRepository;

    /**
     * Constructor of the class.
     * @param medicationRepository the repository to be used by the class.
     * @param residentRepository the repository to be used by the class.
     */
    public MedicationCommandServicesImpl(MedicationRepository medicationRepository,
                                         ResidentRepository residentRepository) {
        this.medicationRepository = medicationRepository;
        this.residentRepository = residentRepository;
    }

    // inherit javadoc
    @Override
    public Long handle(CreateMedicationCommand command) {
        // Validate resident exists
        var resident = residentRepository.findById(command.residentId())
                .orElseThrow(() -> new ResidentNotFoundException(command.residentId()));

        // Validate resident is active
        if (!resident.getResidentStatus().equals(ResidentState.ACTIVE)) {
            throw new ResidentNotActiveException(command.residentId());
        }

        // Validate medication doesn't already exist for this resident
        if (medicationRepository.existsByResidentIdAndName(command.residentId(), command.name())) {
            throw new MedicationAlreadyExistsException(command.name(), command.residentId());
        }

        // Create value objects and medication
        var stock = new Stock(command.amount());
        var expirationDate = new ExpirationDate(command.expirationDate());
        var drugPresentation = DrugPresentation.valueOf(command.drugPresentation());
        var medication = new Medication(command.name(), command.description(), stock,
                expirationDate, drugPresentation, command.dosage(), resident);

        // Save medication
        try {
            medicationRepository.save(medication);
        } catch (Exception e) {
            throw new MedicationCreationException(e.getMessage());
        }

        return medication.getId();
    }
}