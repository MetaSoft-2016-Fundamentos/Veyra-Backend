package com.metasoft.veyra.platform.activities.domain.services;

import com.metasoft.veyra.platform.activities.domain.model.aggregates.Activity;
import com.metasoft.veyra.platform.activities.domain.model.commands.CompleteActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.commands.CreateActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.commands.DeleteActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.commands.UpdateActivityCommand;

import java.util.Optional;

/**
 * ActivityCommandService
 * <p>Service interface that handles write operations on the Activity aggregate.</p>
 */
public interface ActivityCommandService {
    /**
     * Handles the creation of a new activity.
     * @param command the command containing the activity data
     * @return the ID of the newly created activity
     * @see CreateActivityCommand
     */
    Long handle(CreateActivityCommand command);

    /**
     * Handles the update of an existing activity.
     * @param command the command containing the updated activity data
     * @return an {@link Optional} containing the updated activity, or empty if not found
     * @see UpdateActivityCommand
     */
    Optional<Activity> handle(UpdateActivityCommand command);

    /**
     * Handles the deletion of an activity by ID.
     * @param command the command containing the activity ID to delete
     * @see DeleteActivityCommand
     */
    void handle(DeleteActivityCommand command);

    /**
     * Handles advancing the status of an activity through its lifecycle.
     * @param command the command containing the activity ID to advance
     * @see CompleteActivityCommand
     */
    void handle(CompleteActivityCommand command);
}
