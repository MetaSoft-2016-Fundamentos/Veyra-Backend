package com.metasoft.veyra.platform.health.domain.model.aggregates;

import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class VitalSignThreshold extends AuditableAbstractAggregateRoot<VitalSignThreshold> {

    @Embedded
    private ResidentId residentId;

    private Integer heartRateMin;
    private Integer heartRateMax;
    private Integer systolicMax;
    private Integer diastolicMax;
    private Double temperatureMin;
    private Double temperatureMax;
    private Integer oxygenSaturationMin;
    private Integer respiratoryRateMin;
    private Integer respiratoryRateMax;

    protected VitalSignThreshold() {}

    public VitalSignThreshold(
            ResidentId residentId,
            Integer heartRateMin,
            Integer heartRateMax,
            Integer systolicMax,
            Integer diastolicMax,
            Double temperatureMin,
            Double temperatureMax,
            Integer oxygenSaturationMin,
            Integer respiratoryRateMin,
            Integer respiratoryRateMax) {
        this.residentId = residentId;
        this.heartRateMin = heartRateMin;
        this.heartRateMax = heartRateMax;
        this.systolicMax = systolicMax;
        this.diastolicMax = diastolicMax;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.oxygenSaturationMin = oxygenSaturationMin;
        this.respiratoryRateMin = respiratoryRateMin;
        this.respiratoryRateMax = respiratoryRateMax;
    }

    public void update(
            Integer heartRateMin,
            Integer heartRateMax,
            Integer systolicMax,
            Integer diastolicMax,
            Double temperatureMin,
            Double temperatureMax,
            Integer oxygenSaturationMin,
            Integer respiratoryRateMin,
            Integer respiratoryRateMax) {
        this.heartRateMin = heartRateMin;
        this.heartRateMax = heartRateMax;
        this.systolicMax = systolicMax;
        this.diastolicMax = diastolicMax;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.oxygenSaturationMin = oxygenSaturationMin;
        this.respiratoryRateMin = respiratoryRateMin;
        this.respiratoryRateMax = respiratoryRateMax;
    }
}
