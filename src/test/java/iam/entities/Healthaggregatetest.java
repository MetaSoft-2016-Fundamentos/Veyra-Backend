package iam.entities;

import com.metasoft.veyra.platform.health.domain.model.aggregates.Allergy;
import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSign;
import com.metasoft.veyra.platform.health.domain.model.commands.RegisterAllergyCommand;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthAggregateTest {

    @Test
    void shouldCreateAllergyWithCorrectFields() {
        ResidentId residentId = new ResidentId(5L);
        Allergy allergy = new Allergy(
                residentId,
                "Urticaria y picazón",
                "Penicilina",
                TypeOfAllergy.DRUG,
                SeverityLevel.HIGH
        );

        assertEquals("Penicilina", allergy.getAllergenName());
        assertEquals(TypeOfAllergy.DRUG, allergy.getTypeOfAllergy());
        assertEquals(SeverityLevel.HIGH, allergy.getSeverityLevel());
        assertEquals("Urticaria y picazón", allergy.getReaction());
    }

    // ── 2. RegisterAllergyCommand lanza excepción si residentId es null ───────

    @Test
    void shouldThrowWhenRegisterAllergyCommandHasNullResidentId() {
        assertThrows(IllegalArgumentException.class, () ->
                new RegisterAllergyCommand(null, "reacción", "Polen",
                        "ENVIRONMENTAL", "MEDIUM")
        );
    }

    // ── 3. RegisterAllergyCommand lanza excepción si allergenName está en blanco

    @Test
    void shouldThrowWhenAllergenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new RegisterAllergyCommand(1L, "reacción", "  ",
                        "FOOD", "LOW")
        );
    }

    // ════════════════════════════════════════════════════════════════════
    // VitalSign aggregate
    // ════════════════════════════════════════════════════════════════════

    // ── 4. VitalSign nuevo inicia con severidad NORMAL ────────────────────────

    @Test
    void shouldInitializeVitalSignWithNormalSeverity() {
        // ResidentId acepta Long, MeasurementId acepta String
        VitalSign vitalSign = new VitalSign(
                new ResidentId(3L),
                new MeasurementId("100")
        );

        assertEquals(SeverityLevel.NORMAL, vitalSign.getSeverityLevel(),
                "Un signo vital recién creado debe tener severidad NORMAL");
    }

    // ── 5. setSeverityLevel actualiza el nivel correctamente ─────────────────

    @Test
    void shouldUpdateSeverityLevelCorrectly() {
        VitalSign vitalSign = new VitalSign(
                new ResidentId(3L),
                new MeasurementId("101")
        );

        vitalSign.setSeverityLevel(SeverityLevel.CRITICAL);

        assertEquals(SeverityLevel.CRITICAL, vitalSign.getSeverityLevel());
    }
}