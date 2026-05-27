package com.metasoft.veyra.platform.profiles.domain.services;

import com.metasoft.veyra.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.metasoft.veyra.platform.profiles.domain.model.commands.CreateBusinessProfileCommand;

import java.util.Optional;

public interface BusinessProfileCommandService {
    Optional<BusinessProfile> handle(CreateBusinessProfileCommand command);
}
