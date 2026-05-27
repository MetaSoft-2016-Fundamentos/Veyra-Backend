package com.metasoft.veyra.platform.health.domain.model.valueobjects;


import java.util.ArrayList;
import java.util.List;

public record ValidationResult(SeverityLevel severity, List<String> anomalies) {

    public static ValidationResult normal() {
        return new ValidationResult(SeverityLevel.NORMAL, new ArrayList<>());
    }

    public boolean hasAnomalies() {
        return !anomalies.isEmpty();
    }

    public String getDetails() {
        return String.join(", ", anomalies);
    }
}