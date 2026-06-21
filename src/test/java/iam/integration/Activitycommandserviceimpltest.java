package iam.integration;

import com.metasoft.veyra.platform.activities.application.internal.commandservices.ActivityCommandServiceImpl;
import com.metasoft.veyra.platform.activities.application.internal.outboundservices.acl.ActivityExternalServices;
import com.metasoft.veyra.platform.activities.domain.model.aggregates.Activity;
import com.metasoft.veyra.platform.activities.domain.model.commands.CompleteActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.commands.CreateActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityStatus;
import com.metasoft.veyra.platform.activities.infrastructure.persistence.jpa.repositories.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * PRUEBA DE INTEGRACIÓN — verifica la lógica del ActivityCommandServiceImpl
 * mockeando repositorio y servicios externos.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ActivityCommandServiceImplTest {

    @Mock private ActivityRepository activityRepository;
    @Mock private ActivityExternalServices externalServices;

    @InjectMocks
    private ActivityCommandServiceImpl activityCommandService;

    private CreateActivityCommand buildCommand() {
        return new CreateActivityCommand(
                "Fisioterapia", LocalDate.now(),
                LocalTime.of(8, 0), LocalTime.of(9, 0),
                "Sala B", 1L, 10L, 5L
        );
    }

    // ── 1. Crear actividad cuando residente y staff existen ───────────────────

    @Test
    void shouldCreateActivityWhenResidentAndStaffExist() {
        CreateActivityCommand command = buildCommand();

        when(externalServices.residentExists(10L)).thenReturn(true);
        when(externalServices.staffExists(5L)).thenReturn(true);
        when(activityRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> activityCommandService.handle(command));
        verify(activityRepository).save(any(Activity.class));
    }

    // ── 2. Lanza excepción si el residente no existe ──────────────────────────

    @Test
    void shouldThrowWhenResidentDoesNotExist() {
        CreateActivityCommand command = buildCommand();

        when(externalServices.residentExists(10L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> activityCommandService.handle(command));

        assertTrue(ex.getMessage().contains("10"),
                "El mensaje debe indicar el ID del residente no encontrado");
        verify(activityRepository, never()).save(any());
    }

    // ── 3. Lanza excepción si el staff no existe ──────────────────────────────

    @Test
    void shouldThrowWhenStaffDoesNotExist() {
        CreateActivityCommand command = buildCommand();

        when(externalServices.residentExists(10L)).thenReturn(true);
        when(externalServices.staffExists(5L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> activityCommandService.handle(command));

        assertTrue(ex.getMessage().contains("5"));
        verify(activityRepository, never()).save(any());
    }

    // ── 4. Completar actividad existente ──────────────────────────────────────

    @Test
    void shouldCompleteExistingActivity() {
        Activity pendingActivity = new Activity(buildCommand());
        CompleteActivityCommand command = new CompleteActivityCommand(99L);

        when(activityRepository.findById(99L)).thenReturn(Optional.of(pendingActivity));

        activityCommandService.handle(command);

        assertEquals(ActivityStatus.COMPLETED, pendingActivity.getStatus());
        verify(activityRepository).save(pendingActivity);
    }

    // ── 5. Lanza excepción al completar actividad inexistente ─────────────────

    @Test
    void shouldThrowWhenCompletingNonExistentActivity() {
        CompleteActivityCommand command = new CompleteActivityCommand(404L);
        when(activityRepository.findById(404L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> activityCommandService.handle(command));

        assertTrue(ex.getMessage().contains("404"));
    }
}
