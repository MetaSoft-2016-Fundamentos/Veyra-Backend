package com.metasoft.veyra.platform.analytics.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.analytics.domain.model.aggregates.Metric;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricType;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for persisting and querying {@link Metric} aggregates.
 *
 * <p>Provides convenience finder methods and custom queries used by the
 * {@code MetricQueryService} and command services. Query methods return domain
 * aggregates that can be further processed by the application layer.</p>
 */
@Repository
public interface MetricRepository extends JpaRepository<Metric,Long> {
    Optional<Metric>findByNursingHomeIdAndMetricTypeAndEventDate(NursingHomeId nursingHomeId, MetricType metricType, LocalDate eventDate);
    @Query("SELECT m FROM Metric m " +"WHERE m.nursingHomeId = :nursingHomeId " +"AND m.metricType = :metricType " +"AND YEAR(m.eventDate) = :year")
    List<Metric> findByNursingHomeIdAndMetricTypeAndYear(@Param("nursingHomeId") NursingHomeId nursingHomeId,@Param("metricType") MetricType metricType,@Param("year") Integer year);
    @Query("SELECT m FROM Metric m " + "WHERE m.nursingHomeId=:nursingHomeId " +"AND m.metricType=:metricType " +"AND YEAR (m.eventDate)=:year "+"AND MONTH (m.eventDate)=:month")
    List<Metric> findByNursingHomeIdAndMetricTypeAndYearAndMonth(@Param("nursingHomeId") NursingHomeId nursingHomeId,@Param("metricType") MetricType metricType,@Param("year") Integer year, @Param("month") Integer month);
    List<Metric> findByNursingHomeIdAndMetricTypeAndEventDateBetween( NursingHomeId nursingHomeId, MetricType metricType,LocalDate startDate, LocalDate endDate);
}
