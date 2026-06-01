package com.metasoft.veyra.platform.activities.domain.model.aggregates;


import com.metasoft.veyra.platform.activities.domain.model.commands.CreateActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityPeriod;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityStatus;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.Area;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Resident;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "activities")
@Getter
public class Activity extends AuditableAbstractAggregateRoot<Activity> {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate activityDate;

    @Embedded
    private ActivityPeriod period;

    @Embedded
    private Area area;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityStatus status;

    @Embedded
    @AttributeOverride(name = "nursingHomeId", column = @Column(name = "nursing_home_id", nullable = false))
    private NursingHomeId nursingHomeId;

    // --- Relaciones para JPQL ---

    // El Agregado 'Resident' del módulo 'nursing'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", insertable = false, updatable = false)
    private Resident resident;

    @Column(name = "resident_id", nullable = false)
    private Long residentId; // Guardamos solo el ID

    // El Agregado 'Staff' del módulo 'hcm'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_member_id", insertable = false, updatable = false)
    private Staff staff; // 'Attendant'

    @Column(name = "staff_member_id", nullable = false)
    private Long staffMemberId; // Guardamos solo el ID

    // ------------------------------------------------

    public Activity() {
        super();
        this.status = ActivityStatus.PENDING;
    }

    public Activity(CreateActivityCommand command) {
        this();
        this.name = command.name();
        this.activityDate = command.activityDate();
        this.period = new ActivityPeriod(command.startTime(), command.endTime());
        this.area = new Area(command.area());
        this.nursingHomeId = new NursingHomeId(command.nursingHomeId());

        // Asignamos los IDs directamente
        this.residentId = command.residentId();
        this.staffMemberId = command.staffMemberId();
    }

    public void complete() {
        if (this.status != ActivityStatus.PENDING) {
            throw new IllegalStateException("Only pending activities can be completed.");
        }
        this.status = ActivityStatus.COMPLETED;
    }

    public void cancel() {
        if (this.status == ActivityStatus.COMPLETED) {
            throw new IllegalStateException("A completed activity cannot be cancelled.");
        }
        this.status = ActivityStatus.CANCELLED;
    }
}