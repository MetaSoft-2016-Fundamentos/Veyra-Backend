package iam.integration;

import com.metasoft.veyra.platform.hcm.application.internal.commandservices.StaffCommandServiceImpl;
import com.metasoft.veyra.platform.hcm.application.internal.outboundservices.acl.ExternalNursingService;
import com.metasoft.veyra.platform.hcm.application.internal.outboundservices.acl.ExternalProfileService;
import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.hcm.domain.model.commands.CreateStaffCommand;
import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.PersonProfileId;
import com.metasoft.veyra.platform.hcm.infrastructure.persistence.jpa.repositories.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StaffCommandServiceImplTest {

    @Mock private StaffRepository staffRepository;
    @Mock private ExternalNursingService externalNursingService;
    @Mock private ExternalProfileService externalProfileService;

    @InjectMocks
    private StaffCommandServiceImpl staffCommandService;

    private final PersonProfileId personProfileId = new PersonProfileId(10L);
    private final NursingHomeId nursingHomeId = new NursingHomeId(1L);

    private CreateStaffCommand buildCommand() {
        return new CreateStaffCommand(
                1L, "12345678", "Carlos", "Mendez",
                LocalDate.of(1985, 3, 20), 40,
                "carlos@email.com", "Av. Lima", "123",
                "Lima", "15001", "Peru",
                null, "foto.jpg", "987654321",
                "Ana", "Mendez", "999111222"
        );
    }

    @BeforeEach
    void setUp() {
        // Por defecto: nursing home existe, perfil existe, no es residente, no es staff duplicado
        when(externalNursingService.fetchNursingHomeById(1L)).thenReturn(Optional.of(nursingHomeId));
        when(externalProfileService.fetchProfileByDni("12345678")).thenReturn(Optional.of(personProfileId));
        when(externalNursingService.existsResidentByPersonProfile(10L)).thenReturn(false);
        when(staffRepository.findByPersonProfileIdAndNursingHomeId(personProfileId, nursingHomeId))
                .thenReturn(Optional.empty());
        when(staffRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    // ── 1. Crear staff correctamente cuando todo es válido ────────────────────

    @Test
    void shouldCreateStaffSuccessfullyWhenAllDataIsValid() {
        assertDoesNotThrow(() -> staffCommandService.handle(buildCommand()));
        verify(staffRepository).save(any(Staff.class));
    }

    // ── 2. Lanza excepción si la nursing home no existe ───────────────────────

    @Test
    void shouldThrowWhenNursingHomeDoesNotExist() {
        when(externalNursingService.fetchNursingHomeById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> staffCommandService.handle(buildCommand()));

        assertEquals("Nursing home id does not exist", ex.getMessage());
        verify(staffRepository, never()).save(any());
    }

    // ── 3. Lanza excepción si la persona ya está registrada como residente ────

    @Test
    void shouldThrowWhenPersonIsAlreadyAResident() {
        when(externalNursingService.existsResidentByPersonProfile(10L)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> staffCommandService.handle(buildCommand()));

        assertTrue(ex.getMessage().contains("already registered as a resident"));
        verify(staffRepository, never()).save(any());
    }

    // ── 4. Lanza excepción si el staff ya existe en esa nursing home ──────────

    @Test
    void shouldThrowWhenStaffAlreadyExistsInNursingHome() {
        Staff existingStaff = mock(Staff.class);
        when(existingStaff.getPersonProfileId()).thenReturn(personProfileId);
        when(staffRepository.findByPersonProfileIdAndNursingHomeId(personProfileId, nursingHomeId))
                .thenReturn(Optional.of(existingStaff));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> staffCommandService.handle(buildCommand()));

        assertTrue(ex.getMessage().contains("already registered as staff"));
        verify(staffRepository, never()).save(any());
    }
}
