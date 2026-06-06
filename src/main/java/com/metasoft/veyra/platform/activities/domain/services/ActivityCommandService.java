package com.metasoft.veyra.platform.activities.domain.services;

import com.metasoft.veyra.platform.activities.domain.model.commands.CompleteActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.commands.CreateActivityCommand;

public interface ActivityCommandService {
    Long handle(CreateActivityCommand command);
    void handle(CompleteActivityCommand command);
}