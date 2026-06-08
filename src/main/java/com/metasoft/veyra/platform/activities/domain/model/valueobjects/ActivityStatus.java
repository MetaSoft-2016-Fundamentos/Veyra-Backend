package com.metasoft.veyra.platform.activities.domain.model.valueobjects;

/**
 * Represents the lifecycle status of an {@link com.metasoft.veyra.platform.activities.domain.model.aggregates.Activity}.
 * <p>Transitions: PENDING → IN_PROGRESS → COMPLETED.</p>
 */
public enum ActivityStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED
}