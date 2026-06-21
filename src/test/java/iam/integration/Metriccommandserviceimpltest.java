package iam.integration;

import com.metasoft.veyra.platform.analytics.application.internal.commandservices.MetricCommandServiceImpl;
import com.metasoft.veyra.platform.analytics.domain.model.aggregates.Metric;
import com.metasoft.veyra.platform.analytics.domain.model.commands.RecordMetricCommand;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricType;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.analytics.infrastructure.persistence.jpa.repositories.MetricRepository;
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
class MetricCommandServiceImplTest {

    @Mock private MetricRepository metricRepository;

    @InjectMocks
    private MetricCommandServiceImpl metricCommandService;

    private final LocalDate eventDate = LocalDate.of(2025, 6, 1);
    private final NursingHomeId nursingHomeId = new NursingHomeId(1L);

    // ── 1. Si la métrica no existe, crea una nueva con valor 1 ────────────────

    @Test
    void shouldCreateNewMetricWhenNoneExists() {
        RecordMetricCommand command = new RecordMetricCommand(
                1L, MetricType.RESIDENT_ADMISSION, MetricCategory.RESIDENT, eventDate);

        when(metricRepository.findByNursingHomeIdAndMetricTypeAndEventDate(
                any(), eq(MetricType.RESIDENT_ADMISSION), eq(eventDate)))
                .thenReturn(Optional.empty());

        metricCommandService.handle(command);

        // Debe guardar una nueva métrica
        verify(metricRepository).save(any(Metric.class));
    }

    // ── 2. Si la métrica ya existe, incrementa su valor ───────────────────────

    @Test
    void shouldIncrementExistingMetricValue() {
        RecordMetricCommand command = new RecordMetricCommand(
                1L, MetricType.EMPLOYEE_HIRED, MetricCategory.STAFF, eventDate);

        Metric existingMetric = new Metric(nursingHomeId, MetricType.EMPLOYEE_HIRED,
                MetricCategory.STAFF, eventDate);

        when(metricRepository.findByNursingHomeIdAndMetricTypeAndEventDate(
                any(), eq(MetricType.EMPLOYEE_HIRED), eq(eventDate)))
                .thenReturn(Optional.of(existingMetric));

        metricCommandService.handle(command);

        // El valor debe haberse incrementado de 1 a 2
        assertEquals(2L, existingMetric.getValue(),
                "Al existir la métrica, el valor debe incrementarse a 2");
        verify(metricRepository).save(existingMetric);
    }

    // ── 3. Dos eventos distintos del mismo día generan métricas distintas ─────

    @Test
    void shouldCreateSeparateMetricsForDifferentTypes() {
        RecordMetricCommand admissionCmd = new RecordMetricCommand(
                1L, MetricType.RESIDENT_ADMISSION, MetricCategory.RESIDENT, eventDate);
        RecordMetricCommand retiredCmd = new RecordMetricCommand(
                1L, MetricType.RESIDENT_RETIRED, MetricCategory.RESIDENT, eventDate);

        when(metricRepository.findByNursingHomeIdAndMetricTypeAndEventDate(
                any(), any(), eq(eventDate))).thenReturn(Optional.empty());

        metricCommandService.handle(admissionCmd);
        metricCommandService.handle(retiredCmd);

        // Se deben crear 2 métricas distintas (una por cada tipo)
        verify(metricRepository, times(2)).save(any(Metric.class));
    }
}
