package com.metasoft.veyra.platform.activities.domain.model.aggregates;

import com.metasoft.veyra.platform.activities.domain.model.commands.CreateActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.commands.UpdateActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityStatus;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityType;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.RecurringDay;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity aggregate root.
 * <p>
 * Represents a care activity assigned to a resident within a nursing home.
 * An activity has a type, title, status lifecycle (PENDING → IN_PROGRESS → COMPLETED),
 * and optional recurrence configuration.
 * </p>
 * @see ActivityType
 * @see ActivityStatus
 * @see RecurringDay
 * @since 1.0
 */
@Entity
@Table(name = "activities")
@Getter
public class Activity extends AuditableAbstractAggregateRoot<Activity> {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType type;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityStatus status;

    @Column(nullable = false)
    private Long nursingHomeId;

    @Column(nullable = false)
    private Long residentId;

    @Column(nullable = false)
    private Long healthcareStaffId;

    @Column(nullable = false)
    private Boolean isRecurring;

    @ElementCollection(targetClass = RecurringDay.class)
    @CollectionTable(name = "activity_recurring_days", joinColumns = @JoinColumn(name = "activity_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "day")
    private List<RecurringDay> recurringDays = new ArrayList<>();

    /**
     * Default constructor required by JPA.
     */
    protected Activity() {}

    /**
     * Creates a new Activity from a {@link CreateActivityCommand}.
     * The initial status is set to {@code PENDING}.
     * @param command the command containing all data required to create the activity
     */
    public Activity(CreateActivityCommand command) {
        this.nursingHomeId = command.nursingHomeId();
        this.type = command.type();
        this.title = command.title();
        this.status = ActivityStatus.PENDING;
        this.residentId = command.residentId();
        this.healthcareStaffId = command.healthcareStaffId();
        this.isRecurring = command.isRecurring();
        this.recurringDays = command.recurringDays() != null ? new ArrayList<>(command.recurringDays()) : new ArrayList<>();
    }

    /**
     * Updates the mutable fields of this activity.
     * @param command the command containing the new values for type, title, isRecurring and recurringDays
     * @return this activity instance after update
     */
    public Activity update(UpdateActivityCommand command) {
        this.type = command.type();
        this.title = command.title();
        this.isRecurring = command.isRecurring();
        this.recurringDays = command.recurringDays() != null ? new ArrayList<>(command.recurringDays()) : new ArrayList<>();
        return this;
    }

    /**
     * Advances the activity status through the lifecycle: PENDING → IN_PROGRESS → COMPLETED.
     * @throws IllegalStateException if the activity is already COMPLETED
     */
    public void advance() {
        if (this.status == ActivityStatus.COMPLETED) {
            throw new IllegalStateException("Activity is already completed.");
        }
        this.status = this.status == ActivityStatus.PENDING
                ? ActivityStatus.IN_PROGRESS
                : ActivityStatus.COMPLETED;
    }
}
