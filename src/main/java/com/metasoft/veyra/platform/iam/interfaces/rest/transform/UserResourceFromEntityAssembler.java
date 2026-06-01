package com.metasoft.veyra.platform.iam.interfaces.rest.transform;

import com.metasoft.veyra.platform.iam.domain.model.aggregates.User;
import com.metasoft.veyra.platform.iam.domain.model.entities.Role;
import com.metasoft.veyra.platform.iam.interfaces.rest.resources.UserResource;

/**
 * Assembler to convert a User entity to a UserResource.
 */
public class UserResourceFromEntityAssembler {
    /**
     * Private constructor to prevent instantiation.
     */
    private UserResourceFromEntityAssembler() {
        // Utility class
    }

    /**
     * Converts a User entity to a UserResource.
     *
     * @param user The {@link User} entity to convert.
     * @return The {@link UserResource} resource that results from the conversion.
     */
    public static UserResource toResourceFromEntity(User user) {
        var roles = user.getRoles().stream().map(Role::getStringName).toList();
        return new UserResource(user.getId(), user.getUsername(), roles);
    }
}
