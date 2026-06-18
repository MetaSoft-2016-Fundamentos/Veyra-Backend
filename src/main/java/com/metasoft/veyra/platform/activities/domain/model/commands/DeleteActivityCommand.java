package com.metasoft.veyra.platform.activities.domain.model.commands;

/**
 * Command to delete an activity by its ID.
 * @param activityId the ID of the activity to delete
 */
public record DeleteActivityCommand(Long activityId) {}
