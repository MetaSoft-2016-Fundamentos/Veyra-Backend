package iam.entities;

import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricType;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.analytics.domain.model.aggregates.Metric;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MetricAggregateTest {

    private Metric buildMetric() {
        return new Metric(
                new NursingHomeId(1L),
                MetricType.RESIDENT_ADMISSION,
                MetricCategory.RESIDENT,
                LocalDate.of(2025, 1, 15)
        );
    }

    @Test
    void shouldInitializeValueToOneOnCreation() {
        Metric metric = buildMetric();

        assertEquals(1L, metric.getValue(),
                "El valor inicial de una métrica nueva debe ser 1");
    }

    @Test
    void shouldIncrementValueByOne() {
        Metric metric = buildMetric();

        metric.incrementValue();

        assertEquals(2L, metric.getValue());
    }

    // ── 3. Múltiples incrementos acumulan correctamente ───────────────────────

    @Test
    void shouldAccumulateMultipleIncrements() {
        Metric metric = buildMetric();

        metric.incrementValue();
        metric.incrementValue();
        metric.incrementValue();

        assertEquals(4L, metric.getValue(),
                "Después de 3 incrementos el valor debe ser 4 (1 inicial + 3)");
    }

    // ── 4. Los campos se almacenan correctamente ──────────────────────────────

    @Test
    void shouldStoreFieldsCorrectly() {
        Metric metric = buildMetric();

        assertEquals(MetricType.RESIDENT_ADMISSION, metric.getMetricType());
        assertEquals(MetricCategory.RESIDENT, metric.getMetricCategory());
        assertEquals(LocalDate.of(2025, 1, 15), metric.getEventDate());
    }
}
