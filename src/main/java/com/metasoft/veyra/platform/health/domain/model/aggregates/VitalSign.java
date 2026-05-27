package com.metasoft.veyra.platform.health.domain.model.aggregates;

import com.metasoft.veyra.platform.health.domain.model.valueobjects.MeasurementId;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.SeverityLevel;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class VitalSign extends AuditableAbstractAggregateRoot<VitalSign> {

    @Embedded
    private ResidentId residentId;

    @Embedded
    private MeasurementId measurementId;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeverityLevel severityLevel;

    protected VitalSign() {
    }

    public VitalSign(ResidentId residentId, MeasurementId measurementId) {
        this.residentId = residentId;
        this.measurementId = measurementId;
        this.severityLevel = SeverityLevel.NORMAL;
    }

}
