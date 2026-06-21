package iam.integration;

import com.metasoft.veyra.platform.health.application.internal.commandservices.AllergyCommandServiceImpl;
import com.metasoft.veyra.platform.health.application.internal.commandservices.VitalSignCommandServiceImpl;
import com.metasoft.veyra.platform.health.application.internal.outboundservices.acl.ExternalNursingService;
import com.metasoft.veyra.platform.health.application.internal.outboundservices.acl.ExternalTrackingService;
import com.metasoft.veyra.platform.health.domain.model.aggregates.Allergy;
import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSign;
import com.metasoft.veyra.platform.health.domain.model.commands.RegisterAllergyCommand;
import com.metasoft.veyra.platform.health.domain.model.commands.ValidateVitalSignCommand;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.SeverityLevel;
import com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories.AllergyRepository;
import com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories.VitalSignRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class HealthCommandServicesImplTest {

    // ── Allergy dependencies ──────────────────────────────────────────────────
    @Mock private AllergyRepository allergyRepository;
    @Mock private ExternalNursingService externalNursingService;
    @InjectMocks private AllergyCommandServiceImpl allergyCommandService;

    // ── VitalSign dependencies ────────────────────────────────────────────────
    @Mock private VitalSignRepository vitalSignRepository;
    @Mock private ExternalTrackingService externalTrackingService;
    @Mock private ApplicationEventPublisher eventPublisher;
    @InjectMocks private VitalSignCommandServiceImpl vitalSignCommandService;

    private final ResidentId residentId = new ResidentId(7L);

    // ════════════════════════════════════════════════════════════════════
    // AllergyCommandServiceImpl
    // ════════════════════════════════════════════════════════════════════

    // ── 1. Registrar alergia correctamente cuando el residente existe ─────────

    @Test
    void shouldRegisterAllergyWhenResidentExists() {
        RegisterAllergyCommand command = new RegisterAllergyCommand(
                7L, "Urticaria", "Penicilina", "DRUG", "HIGH");

        when(externalNursingService.fetchResidentById(7L)).thenReturn(Optional.of(residentId));
        when(allergyRepository.existsByResidentIdAndAllergenName(residentId, "Penicilina"))
                .thenReturn(false);
        when(allergyRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> allergyCommandService.handle(command));
        verify(allergyRepository).save(any(Allergy.class));
    }

    // ── 2. Lanza excepción si el residente no existe ──────────────────────────

    @Test
    void shouldThrowWhenResidentNotFound() {
        RegisterAllergyCommand command = new RegisterAllergyCommand(
                99L, "reacción", "Polen", "ENVIRONMENTAL", "MEDIUM");

        when(externalNursingService.fetchResidentById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> allergyCommandService.handle(command));

        assertEquals("resident id not found", ex.getMessage());
        verify(allergyRepository, never()).save(any());
    }

    // ── 3. Lanza excepción si la alergia ya está registrada ───────────────────

    @Test
    void shouldThrowWhenAllergyAlreadyExists() {
        RegisterAllergyCommand command = new RegisterAllergyCommand(
                7L, "reacción", "Penicilina", "DRUG", "HIGH");

        when(externalNursingService.fetchResidentById(7L)).thenReturn(Optional.of(residentId));
        when(allergyRepository.existsByResidentIdAndAllergenName(residentId, "Penicilina"))
                .thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> allergyCommandService.handle(command));

        assertTrue(ex.getMessage().contains("allergy already exists"));
        verify(allergyRepository, never()).save(any());
    }

    // ════════════════════════════════════════════════════════════════════
    // VitalSignCommandServiceImpl
    // ════════════════════════════════════════════════════════════════════

    // ── 4. Signos vitales normales → severidad NORMAL, sin evento ────────────

    @Test
    void shouldSaveVitalSignWithNormalSeverityWhenAllValuesAreNormal() {
        ValidateVitalSignCommand command = new ValidateVitalSignCommand(
                "m-001", "device-01",
                75,         // heartRate normal (60-100)
                120,        // systolic normal (<140)
                80,         // diastolic normal (<90)
                37.0,       // temperature normal (36.1-37.5)
                98,         // oxygenSaturation normal (>=95)
                16          // respiratoryRate normal (12-20)
        );

        when(externalTrackingService.fetchDeviceIdByResidentId("device-01"))
                .thenReturn(Optional.of(residentId));
        when(vitalSignRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        vitalSignCommandService.handle(command);

        ArgumentCaptor<VitalSign> captor = ArgumentCaptor.forClass(VitalSign.class);
        verify(vitalSignRepository).save(captor.capture());
        assertEquals(SeverityLevel.NORMAL, captor.getValue().getSeverityLevel());
        // Sin anomalías → no se publica evento
        verify(eventPublisher, never()).publishEvent(any());
    }

    // ── 5. Signos vitales críticos → severidad CRITICAL, publica evento ───────

    @Test
    void shouldSaveVitalSignWithCriticalSeverityAndPublishEventWhenValuesCritical() {
        ValidateVitalSignCommand command = new ValidateVitalSignCommand(
                "m-002", "device-02",
                130,        // heartRate > HR_CRITICAL_HIGH (120) → critical
                120,
                80,
                37.0,
                98,
                16
        );

        when(externalTrackingService.fetchDeviceIdByResidentId("device-02"))
                .thenReturn(Optional.of(residentId));
        when(vitalSignRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        vitalSignCommandService.handle(command);

        ArgumentCaptor<VitalSign> captor = ArgumentCaptor.forClass(VitalSign.class);
        verify(vitalSignRepository).save(captor.capture());
        assertEquals(SeverityLevel.CRITICAL, captor.getValue().getSeverityLevel());
        // Con anomalía crítica → debe publicar el evento de alerta
        verify(eventPublisher).publishEvent(any());
    }

    // ── 6. Dispositivo sin residente asignado → no se guarda nada ─────────────

    @Test
    void shouldSkipWhenDeviceHasNoResidentAssigned() {
        ValidateVitalSignCommand command = new ValidateVitalSignCommand(
                "m-003", "unknown-device",
                75, 120, 80, 37.0, 98, 16
        );

        when(externalTrackingService.fetchDeviceIdByResidentId("unknown-device"))
                .thenReturn(Optional.empty());

        vitalSignCommandService.handle(command);

        verify(vitalSignRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }
}
