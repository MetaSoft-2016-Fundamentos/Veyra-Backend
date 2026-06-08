package com.metasoft.veyra.platform.activities.domain.model.commands;

/**
 * Command to advance an activity's status through its lifecycle.
 * Transitions: PENDING → IN_PROGRESS → COMPLETED.
 * @param activityId the ID of the activity to advance
 */
public record CompleteActivityCommand(Long activityId) {}