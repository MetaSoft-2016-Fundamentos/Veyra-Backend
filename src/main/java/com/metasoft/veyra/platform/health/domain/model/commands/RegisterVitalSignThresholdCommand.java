package com.metasoft.veyra.platform.health.domain.model.commands;

public record RegisterVitalSignThresholdCommand(
        Long residentId,
        Integer heartRateMin,
        Integer heartRateMax,
        Integer systolicMax,
        Integer diastolicMax,
        Double temperatureMin,
        Double temperatureMax,
        Integer oxygenSaturationMin,
        Integer respiratoryRateMin,
        Integer respiratoryRateMax
) {}
