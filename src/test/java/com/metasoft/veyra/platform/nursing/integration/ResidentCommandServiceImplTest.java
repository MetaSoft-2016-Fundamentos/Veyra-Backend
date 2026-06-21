package com.metasoft.veyra.platform.nursing.integration;

import com.metasoft.veyra.platform.nursing.application.internal.commandservices.ResidentCommandServiceImpl;
import com.metasoft.veyra.platform.nursing.application.internal.outboundservices.acl.ExternalProfileService;
import com.metasoft.veyra.platform.nursing.domain.exceptions.NursingHomeNotFoundException;
import com.metasoft.veyra.platform.nursing.domain.exceptions.ResidentAlreadyExistsException;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.NursingHome;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Resident;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateResidentCommand;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.NursingHomeRepository;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.ResidentRepository;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.PersonProfileId;

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
class ResidentCommandServiceImplTest {

    @Mock
    private ExternalProfileService externalProfileService;
    @Mock
    private ResidentRepository residentRepository;
    @Mock
    private NursingHomeRepository nursingHomeRepository;

    @InjectMocks
    private ResidentCommandServiceImpl residentCommandService;

    private CreateResidentCommand crearComandoDummy() {
        return new CreateResidentCommand(
                1L, "72345678", "Roberto", "Gomez",
                LocalDate.of(1945, 5, 10), 80, "roberto@correo.com",
                "Av. Siempre Viva", "123", "Lima", "15001", "Perú",
                new byte[0], "foto.png", "987654321",
                "Carlos", "Gomez", "999888777",
                "Maria", "Perez", "999111222"
        );
    }

    @Test
    @DisplayName("Debe crear un residente exitosamente cuando el perfil ya existe en el sistema")
    void handle_CrearResidente_Exito() {

        CreateResidentCommand command = crearComandoDummy();

        NursingHome mockNursingHome = mock(NursingHome.class);
        when(mockNursingHome.getId()).thenReturn(command.nursingHomeId());
        when(nursingHomeRepository.findById(command.nursingHomeId())).thenReturn(Optional.of(mockNursingHome));

        PersonProfileId mockProfileId = new PersonProfileId(100L);
        when(externalProfileService.fetchProfileByDni(command.dni())).thenReturn(Optional.of(mockProfileId));

        when(residentRepository.existsByNursingHomeIdAndPersonProfileId(command.nursingHomeId(), mockProfileId))
                .thenReturn(false);

        Long resultId = residentCommandService.handle(command);

        verify(residentRepository, times(1)).save(any(Resident.class));
        verify(externalProfileService, never()).createPersonProfile(
                anyString(), anyString(), anyString(), any(), anyInt(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), any(), anyString(), anyString()
        );
    }

    @Test
    @DisplayName("Debe lanzar NursingHomeNotFoundException cuando el ID de la casa de reposo no existe")
    void handle_CrearResidente_FallaPorNursingHomeInexistente() {

        CreateResidentCommand command = crearComandoDummy();

        when(nursingHomeRepository.findById(command.nursingHomeId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> residentCommandService.handle(command))
                .isInstanceOf(NursingHomeNotFoundException.class);

        verify(externalProfileService, never()).fetchProfileByDni(anyString());
        verify(residentRepository, never()).save(any(Resident.class));
    }

    @Test
    @DisplayName("Debe lanzar ResidentAlreadyExistsException cuando se intenta registrar un residente que ya está en la casa de reposo")
    void handle_CrearResidente_FallaPorResidenteDuplicado() {

        CreateResidentCommand command = crearComandoDummy();

        NursingHome mockNursingHome = mock(NursingHome.class);
        when(nursingHomeRepository.findById(command.nursingHomeId())).thenReturn(Optional.of(mockNursingHome));

        PersonProfileId mockProfileId = new PersonProfileId(100L);
        when(externalProfileService.fetchProfileByDni(command.dni())).thenReturn(Optional.of(mockProfileId));

        when(residentRepository.existsByNursingHomeIdAndPersonProfileId(command.nursingHomeId(), mockProfileId))
                .thenReturn(true);

        assertThatThrownBy(() -> residentCommandService.handle(command))
                .isInstanceOf(ResidentAlreadyExistsException.class);

        verify(residentRepository, never()).save(any(Resident.class));
    }
}