package com.metasoft.veyra.platform.tracking.domain.model.valueobjects;


public record BloodPressure(Integer systolic, Integer diastolic) {

    public BloodPressure {
        if (systolic == null || systolic < 0 || systolic > 300) {
            throw new IllegalArgumentException("Systolic pressure must be between 0 and 300 mmHg");
        }
        if (diastolic == null || diastolic < 0 || diastolic > 200) {
            throw new IllegalArgumentException("Diastolic pressure must be between 0 and 200 mmHg");
        }
        if (systolic <= diastolic) {
            throw new IllegalArgumentException("Systolic must be greater than diastolic");
        }
    }

}