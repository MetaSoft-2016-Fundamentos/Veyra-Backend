package com.metasoft.veyra.platform.hcm.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;
@Embeddable
public record ContractPeriod(LocalDate startDate, LocalDate endDate) {

}
