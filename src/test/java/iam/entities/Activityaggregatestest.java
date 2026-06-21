package iam.entities;

import com.metasoft.veyra.platform.activities.domain.model.aggregates.Activity;
import com.metasoft.veyra.platform.activities.domain.model.commands.CreateActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;


class ActivityAggregateTest {

    private CreateActivityCommand buildCommand() {
        return new CreateActivityCommand(
                "Terapia ocupacional",
                LocalDate.of(2025, 6, 10),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                "Sala A",
                1L,
                2L,
                3L
        );
    }

    // ── 1. Estado inicial siempre es PENDING ──────────────────────────────────

    @Test
    void shouldInitializeWithPendingStatus() {
        Activity activity = new Activity(buildCommand());

        assertEquals(ActivityStatus.PENDING, activity.getStatus(),
                "Una actividad nueva debe iniciar con estado PENDING");
    }

    // ── 2. Completar una actividad PENDING → COMPLETED ────────────────────────

    @Test
    void shouldCompleteWhenStatusIsPending() {
        Activity activity = new Activity(buildCommand());

        activity.complete();

        assertEquals(ActivityStatus.COMPLETED, activity.getStatus());
    }

    // ── 3. Cancelar una actividad PENDING → CANCELLED ─────────────────────────

    @Test
    void shouldCancelWhenStatusIsPending() {
        Activity activity = new Activity(buildCommand());

        activity.cancel();

        assertEquals(ActivityStatus.CANCELLED, activity.getStatus());
    }

    // ── 4. No se puede completar una actividad ya COMPLETED ───────────────────

    @Test
    void shouldThrowWhenCompletingAlreadyCompletedActivity() {
        Activity activity = new Activity(buildCommand());
        activity.complete();

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                activity::complete);

        assertEquals("Only pending activities can be completed.", ex.getMessage());
    }

    // ── 5. No se puede cancelar una actividad COMPLETED ───────────────────────

    @Test
    void shouldThrowWhenCancellingCompletedActivity() {
        Activity activity = new Activity(buildCommand());
        activity.complete();

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                activity::cancel);

        assertEquals("A completed activity cannot be cancelled.", ex.getMessage());
    }

    // ── 6. Los datos del comando se almacenan correctamente ───────────────────

    @Test
    void shouldStoreCommandDataCorrectly() {
        CreateActivityCommand command = buildCommand();
        Activity activity = new Activity(command);

        assertEquals("Terapia ocupacional", activity.getName());
        assertEquals(LocalDate.of(2025, 6, 10), activity.getActivityDate());
        assertEquals(2L, activity.getResidentId());
        assertEquals(3L, activity.getStaffMemberId());
    }
}
