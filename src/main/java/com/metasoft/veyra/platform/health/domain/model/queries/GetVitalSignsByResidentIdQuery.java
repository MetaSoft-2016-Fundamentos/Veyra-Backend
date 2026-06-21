package com.metasoft.veyra.platform.health.domain.model.queries;

import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public record GetVitalSignsByResidentIdQuery(
  ResidentId residentId,
  LocalDateTime startDate,
  LocalDateTime endDate,
  Pageable pageable
) {
  public GetVitalSignsByResidentIdQuery {
    if (residentId == null) {
      throw new IllegalArgumentException("Resident id cannot be null");
    }
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("Start date and End date cannot be null");
    }
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after end date");
    }
    if (pageable == null) {
      throw new IllegalArgumentException("Pageable cannot be null");
    }
  }
}
