package com.metasoft.veyra.platform.nursing.integration;

import com.metasoft.veyra.platform.nursing.application.internal.commandservices.MedicationCommandServicesImpl;
import com.metasoft.veyra.platform.nursing.domain.exceptions.MedicationAlreadyExistsException;
import com.metasoft.veyra.platform.nursing.domain.exceptions.ResidentNotActiveException;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Medication;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Resident;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateMedicationCommand;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.ResidentState;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.MedicationRepository;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.ResidentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationCommandServicesImplTest {

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private ResidentRepository residentRepository;

    @InjectMocks
    private MedicationCommandServicesImpl medicationCommandServices;

    private CreateMedicationCommand crearComandoDummy() {
        return new CreateMedicationCommand(
                "Aspirina", "Para el corazón", 30,
                LocalDate.now().plusMonths(6), "TABLET", "1 diaria", 1L
        );
    }

    @Test
    @DisplayName("Debe crear y guardar un medicamento exitosamente")
    void handle_CrearMedicamento_Exito() {

        CreateMedicationCommand command = crearComandoDummy();

        Resident mockResident = mock(Resident.class);
        when(mockResident.getResidentStatus()).thenReturn(ResidentState.ACTIVE);

        when(residentRepository.findById(command.residentId())).thenReturn(Optional.of(mockResident));
        when(medicationRepository.existsByResidentIdAndName(command.residentId(), command.name())).thenReturn(false);

        medicationCommandServices.handle(command);

        verify(medicationRepository, times(1)).save(any(Medication.class));
    }

    @Test
    @DisplayName("Debe lanzar ResidentNotActiveException si el residente está retirado o fallecido")
    void handle_CrearMedicamento_FallaPorResidenteInactivo() {

        CreateMedicationCommand command = crearComandoDummy();

        Resident mockResident = mock(Resident.class);

        when(mockResident.getResidentStatus()).thenReturn(ResidentState.DECEASED);

        when(residentRepository.findById(command.residentId())).thenReturn(Optional.of(mockResident));

        assertThatThrownBy(() -> medicationCommandServices.handle(command))
                .isInstanceOf(ResidentNotActiveException.class);

        verify(medicationRepository, never()).save(any(Medication.class));
    }

    @Test
    @DisplayName("Debe lanzar MedicationAlreadyExistsException si el residente ya tiene recetado ese medicamento")
    void handle_CrearMedicamento_FallaPorMedicamentoDuplicado() {

        CreateMedicationCommand command = crearComandoDummy();

        Resident mockResident = mock(Resident.class);
        when(mockResident.getResidentStatus()).thenReturn(ResidentState.ACTIVE);

        when(residentRepository.findById(command.residentId())).thenReturn(Optional.of(mockResident));

        when(medicationRepository.existsByResidentIdAndName(command.residentId(), command.name())).thenReturn(true);

        assertThatThrownBy(() -> medicationCommandServices.handle(command))
                .isInstanceOf(MedicationAlreadyExistsException.class);

        verify(medicationRepository, never()).save(any(Medication.class));
    }
}